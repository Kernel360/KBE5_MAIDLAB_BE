package kernel.maidlab.core.aop.aspect.exception;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import kernel.maidlab.core.aop.enums.LogLevel;
import kernel.maidlab.common.enums.ResponseType;
import kernel.maidlab.core.aop.annotation.exception.ExceptionHandler;
import kernel.maidlab.core.exception.BaseException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Aspect
@Component
public class ExceptionHandlingAspect {

	@Value("${notification.discord.webhook-url:}")
	private String discordWebhookUrl;

	private final RestTemplate restTemplate = new RestTemplate();

	@Around("@annotation(exceptionHandler)")
	public Object handleException(ProceedingJoinPoint joinPoint, ExceptionHandler exceptionHandler) throws Throwable {

		String methodName = joinPoint.getSignature().getName();
		String className = joinPoint.getTarget().getClass().getSimpleName();

		try {
			return joinPoint.proceed();

		} catch (BaseException e) {
			logException(className, methodName, e, exceptionHandler.logLevel());

			if (exceptionHandler.enableNotification()) {
				notifyException(className, methodName, e);
			}

			throw e;

		} catch (Exception e) {
			if (shouldHandleException(e, exceptionHandler.value())) {
				logException(className, methodName, e, exceptionHandler.logLevel());

				if (exceptionHandler.enableNotification()) {
					notifyException(className, methodName, e);
				}

				ResponseType responseType = exceptionHandler.responseType();
				String message = exceptionHandler.message().isEmpty() ?
					responseType.getMessage() : exceptionHandler.message();

				throw new BaseException(responseType) {
					@Override
					public String getMessage() {
						return message;
					}
				};
			} else {
				throw e;
			}
		}
	}

	// 예외처리 검증
	private boolean shouldHandleException(Exception exception, Class<? extends Throwable>[] targetExceptions) {
		if (targetExceptions.length == 0) {
			return true; // 모든 예외 처리
		}

		return Arrays.stream(targetExceptions)
			.anyMatch(targetException -> targetException.isAssignableFrom(exception.getClass()));
	}

	// 예외 로깅
	private void logException(String className, String methodName, Exception e, LogLevel logLevel) {
		String message = "예외 처리 - {}#{} - {}: {}";

		switch (logLevel) {
			case DEBUG:
				log.debug(message, className, methodName, e.getClass().getSimpleName(), e.getMessage());
				break;
			case INFO:
				log.info(message, className, methodName, e.getClass().getSimpleName(), e.getMessage());
				break;
			case WARN:
				log.warn(message, className, methodName, e.getClass().getSimpleName(), e.getMessage());
				break;
			case ERROR:
				log.error(message, className, methodName, e.getClass().getSimpleName(), e.getMessage(), e);
				break;
		}
	}

	// 예외 알림
	private void notifyException(String className, String methodName, Exception e) {
		// 기존 로그
		log.warn("🚨 예외 알림 - {}#{} - {}: {}", className, methodName, e.getClass().getSimpleName(), e.getMessage());

		// Discord 웹훅 알림
		if (!discordWebhookUrl.isEmpty()) {
			sendDiscordNotification(className, methodName, e);
		}
	}

	// Discord 알림 메서드
	private void sendDiscordNotification(String className, String methodName, Exception e) {
		try {
			Map<String, Object> payload = createDiscordPayload(className, methodName, e);

			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);

			HttpEntity<Map<String, Object>> request = new HttpEntity<>(payload, headers);
			restTemplate.postForEntity(discordWebhookUrl, request, String.class);

			log.debug("Discord 알림 전송 완료: {}#{}", className, methodName);
		} catch (Exception ex) {
			log.error("Discord 알림 전송 실패", ex);
		}
	}

	// Discord 메시지 페이로드 생성
	private Map<String, Object> createDiscordPayload(String className, String methodName, Exception e) {
		String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

		Map<String, Object> embed = new HashMap<>();
		embed.put("title", "🚨 예외 발생 🚨");
		embed.put("color", 15158332); // 빨간색
		embed.put("timestamp", LocalDateTime.now().toString());

		Map<String, Object> field1 = new HashMap<>();
		field1.put("name", "- 위치");
		field1.put("value", className + "#" + methodName);
		field1.put("inline", false);

		Map<String, Object> field2 = new HashMap<>();
		field2.put("name", "- 예외 타입");
		field2.put("value", e.getClass().getSimpleName());
		field2.put("inline", false);

		Map<String, Object> field3 = new HashMap<>();
		field3.put("name", "- 메시지");
		field3.put("value", "```" + e.getMessage() + "```");
		field3.put("inline", false);

		Map<String, Object> field4 = new HashMap<>();
		field4.put("name", "- 발생 시간");
		field4.put("value", timestamp);
		field4.put("inline", false);

		embed.put("fields", Arrays.asList(field1, field2, field3, field4));

		Map<String, Object> payload = new HashMap<>();
		payload.put("embeds", List.of(embed));

		return payload;
	}
}