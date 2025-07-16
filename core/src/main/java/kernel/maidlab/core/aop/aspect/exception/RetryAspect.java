package kernel.maidlab.core.aop.aspect.exception;

import java.util.Arrays;
import java.util.List;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import kernel.maidlab.core.aop.enums.RetryStrategy;
import kernel.maidlab.core.aop.annotation.exception.Retry;
import kernel.maidlab.core.exception.util.ExceptionUtils;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Aspect
@Component
public class RetryAspect {

	@Around("@annotation(retry)")
	public Object retryMethod(ProceedingJoinPoint joinPoint, Retry retry) throws Throwable {
		String methodName = joinPoint.getSignature().getName();
		String className = joinPoint.getTarget().getClass().getSimpleName();

		int maxAttempts = retry.maxAttempts();
		long delay = retry.delay();
		double multiplier = retry.multiplier();
		long maxDelay = retry.maxDelay();
		List<Class<? extends Throwable>> retryFor = Arrays.asList(retry.retryFor());
		List<Class<? extends Throwable>> noRetryFor = Arrays.asList(retry.noRetryFor());

		log.debug("재시도 로직 시작 - {}#{}, 최대 시도 횟수: {}", className, methodName, maxAttempts);

		Throwable lastException = null;
		long currentDelay = delay;

		for (int attempt = 1; attempt <= maxAttempts; attempt++) {
			try {
				Object result = joinPoint.proceed();
				if (attempt > 1) {
					log.info("재시도 성공 - {}#{}, 시도 횟수: {}/{}", className, methodName, attempt, maxAttempts);
				}
				return result;

			} catch (Throwable exception) {
				lastException = exception;

				if (attempt == maxAttempts) {
					log.error("최대 재시도 횟수 초과 - {}#{}, 시도 횟수: {}/{}",
						className, methodName, attempt, maxAttempts);
					break;
				}

				if (!shouldRetry(exception, retryFor, noRetryFor)) {
					if (exception instanceof kernel.maidlab.core.exception.BaseException) {
						log.warn("재시도 제외 예외 발생 - {}#{}, 예외 정보: {}",
							className, methodName,
							ExceptionUtils.formatExceptionForLog(
								(kernel.maidlab.core.exception.BaseException)exception));
					} else {
						log.warn("재시도 제외 예외 발생 - {}#{}, 예외: {} - {}",
							className, methodName, exception.getClass().getSimpleName(), exception.getMessage());
					}
					break;
				}

				if (exception instanceof kernel.maidlab.core.exception.BaseException) {
					log.warn("재시도 대기 - {}#{}, 시도 횟수: {}/{}, 대기 시간: {}ms, 예외 정보: {}",
						className, methodName, attempt, maxAttempts, currentDelay,
						ExceptionUtils.formatExceptionForLog((kernel.maidlab.core.exception.BaseException)exception));
				} else {
					log.warn("재시도 대기 - {}#{}, 시도 횟수: {}/{}, 대기 시간: {}ms, 예외: {} - {}",
						className, methodName, attempt, maxAttempts, currentDelay,
						exception.getClass().getSimpleName(), exception.getMessage());
				}

				try {
					Thread.sleep(currentDelay);
				} catch (InterruptedException ie) {
					Thread.currentThread().interrupt();
					throw ie;
				}

				currentDelay = calculateNextDelay(currentDelay, multiplier, maxDelay, retry.strategy());
			}
		}

		// lastException이 null인 경우는 이론적으로 불가능하지만 안전을 위해 체크
		if (lastException != null) {
			throw lastException;
		} else {
			throw new RuntimeException("재시도 중 예상치 못한 오류 발생 - " + className + "#" + methodName);
		}
	}

	private boolean shouldRetry(Throwable exception, List<Class<? extends Throwable>> retryFor,
		List<Class<? extends Throwable>> noRetryFor) {

		// noRetryFor에 포함된 예외는 재시도하지 않음
		for (Class<? extends Throwable> noRetryClass : noRetryFor) {
			if (noRetryClass.isAssignableFrom(exception.getClass())) {
				return false;
			}
		}

		// retryFor가 비어있으면 모든 예외에 대해 재시도
		if (retryFor.isEmpty()) {
			return true;
		}

		// retryFor에 포함된 예외만 재시도
		for (Class<? extends Throwable> retryClass : retryFor) {
			if (retryClass.isAssignableFrom(exception.getClass())) {
				return true;
			}
		}

		return false;
	}

	private long calculateNextDelay(long currentDelay, double multiplier, long maxDelay,
		RetryStrategy strategy) {

		long nextDelay = switch (strategy) {
			case FIXED -> currentDelay;
			case LINEAR -> currentDelay + (long)(currentDelay * multiplier);
			case EXPONENTIAL -> (long)(currentDelay * multiplier);
		};

		return Math.min(nextDelay, maxDelay);
	}
}