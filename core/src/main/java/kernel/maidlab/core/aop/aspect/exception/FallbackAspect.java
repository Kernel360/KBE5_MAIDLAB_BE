package kernel.maidlab.core.aop.aspect.exception;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import kernel.maidlab.core.aop.annotation.exception.Fallback;
import kernel.maidlab.core.exception.util.ExceptionUtils;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Aspect
@Component
public class FallbackAspect {

	@Around("@annotation(fallback)")
	public Object fallbackMethod(ProceedingJoinPoint joinPoint, Fallback fallback) throws Throwable {
		String methodName = joinPoint.getSignature().getName();
		String className = joinPoint.getTarget().getClass().getSimpleName();

		try {
			return joinPoint.proceed();

		} catch (Throwable exception) {
			if (shouldApplyFallback(exception, fallback.exceptions())) {
				if (exception instanceof kernel.maidlab.core.exception.BaseException) {
					log.warn("Fallback 적용 - {}#{}, 예외 정보: {}", 
						className, methodName, 
						ExceptionUtils.formatExceptionForLog((kernel.maidlab.core.exception.BaseException) exception));
				} else {
					log.warn("Fallback 적용 - {}#{}, 예외: {} - {}", 
						className, methodName, exception.getClass().getSimpleName(), exception.getMessage());
				}
				return executeFallback(joinPoint, fallback, exception);
			}

			throw exception;
		}
	}

	private boolean shouldApplyFallback(Throwable exception, Class<? extends Throwable>[] exceptions) {
		for (Class<? extends Throwable> exceptionClass : exceptions) {
			if (exceptionClass.isAssignableFrom(exception.getClass())) {
				return true;
			}
		}
		return false;
	}

	private Object executeFallback(ProceedingJoinPoint joinPoint, Fallback fallback, Throwable exception) {
		MethodSignature signature = (MethodSignature)joinPoint.getSignature();
		Class<?> returnType = signature.getReturnType();

		return switch (fallback.type()) {
			case METHOD -> executeFallbackMethod(joinPoint, fallback.method());
			case VALUE -> convertToReturnType(fallback.value(), returnType);
			case EMPTY -> getEmptyValue(returnType);
		};
	}

	private Object executeFallbackMethod(ProceedingJoinPoint joinPoint, String fallbackMethodName) {
		Object target = joinPoint.getTarget();
		Object[] args = joinPoint.getArgs();

		try {
			Method fallbackMethod = findFallbackMethod(target.getClass(), fallbackMethodName, args);
			if (fallbackMethod != null) {
				fallbackMethod.setAccessible(true);
				return fallbackMethod.invoke(target, args);
			}

			log.warn("Fallback 메서드를 찾을 수 없습니다: {}", fallbackMethodName);
			return null;

		} catch (Exception e) {
			log.error("Fallback 메서드 실행 중 예외 발생: {}", e.getMessage(), e);
			return null;
		}
	}

	private Method findFallbackMethod(Class<?> targetClass, String methodName, Object[] args) {
		Method[] methods = targetClass.getDeclaredMethods();

		for (Method method : methods) {
			if (method.getName().equals(methodName) &&
				method.getParameterCount() == args.length) {
				return method;
			}
		}

		return null;
	}

	private Object convertToReturnType(String value, Class<?> returnType) {
		if (returnType == String.class) {
			return value;
		}
		if (returnType == Integer.class || returnType == int.class) {
			return Integer.valueOf(value);
		}
		if (returnType == Long.class || returnType == long.class) {
			return Long.valueOf(value);
		}
		if (returnType == Boolean.class || returnType == boolean.class) {
			return Boolean.valueOf(value);
		}

		return value;
	}

	private Object getEmptyValue(Class<?> returnType) {
		if (returnType == String.class) {
			return "";
		}
		if (returnType == Integer.class || returnType == int.class) {
			return 0;
		}
		if (returnType == Long.class || returnType == long.class) {
			return 0L;
		}
		if (returnType == Boolean.class || returnType == boolean.class) {
			return false;
		}
		if (List.class.isAssignableFrom(returnType)) {
			return Collections.emptyList();
		}
		if (Optional.class.isAssignableFrom(returnType)) {
			return Optional.empty();
		}

		return null;
	}
}