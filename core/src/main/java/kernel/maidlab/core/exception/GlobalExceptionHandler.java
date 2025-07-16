package kernel.maidlab.core.exception;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.catalina.connector.ClientAbortException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.async.AsyncRequestNotUsableException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import jakarta.validation.ConstraintViolationException;
import kernel.maidlab.common.dto.ErrorResponseDto;
import kernel.maidlab.common.enums.ResponseType;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

	@Value("${notification.discord.webhook-url:}")
	private String discordWebhookUrl;

	// 예외 발생 빈도 추적
	private final ConcurrentHashMap<String, AtomicInteger> exceptionCountMap = new ConcurrentHashMap<>();

	@ExceptionHandler(BaseException.class)
	public ResponseEntity<ErrorResponseDto> handleBaseException(BaseException e) {
		ResponseType type = e.getResponseType();

		// 커스텀 메시지가 있으면 사용
		String displayMessage = e.getDisplayMessage();

		// 로그 레벨 분리 (AUTH 관련은 WARN, 나머지는 ERROR)
		if (type.name().contains("AUTH")) {
			log.warn("BaseException [{}] - code: {}, message: {}, timestamp: {}",
				e.getClass().getSimpleName(), type.getCode(), displayMessage, e.getTimestamp());
		} else {
			log.error("BaseException [{}] - code: {}, message: {}, timestamp: {}",
				e.getClass().getSimpleName(), type.getCode(), displayMessage, e.getTimestamp());
		}

		// 원인 예외가 있으면 로그에 포함
		if (e.getCause() != null) {
			log.error("Root cause: {}", e.getCause().getMessage(), e.getCause());
		}

		trackException(e.getClass().getSimpleName());

		return ResponseEntity
			.status(type.getHttpStatus())
			.body(new ErrorResponseDto(type));
	}

	// 유효성 검증 실패 예외 처리
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ErrorResponseDto> handleValidationException(MethodArgumentNotValidException e) {
		log.warn("Validation failed: {}", e.getBindingResult().getAllErrors().getFirst().getDefaultMessage());
		trackException("ValidationException");

		return ResponseEntity
			.status(HttpStatus.BAD_REQUEST)
			.body(new ErrorResponseDto(ResponseType.VALIDATION_FAILED));
	}

	@ExceptionHandler(BindException.class)
	public ResponseEntity<ErrorResponseDto> handleBindException(BindException e) {
		log.warn("Bind exception: {}", e.getBindingResult().getAllErrors().getFirst().getDefaultMessage());
		trackException("BindException");

		return ResponseEntity
			.status(HttpStatus.BAD_REQUEST)
			.body(new ErrorResponseDto(ResponseType.VALIDATION_FAILED));
	}

	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<ErrorResponseDto> handleConstraintViolationException(ConstraintViolationException e) {
		log.warn("Constraint violation: {}", e.getMessage());
		trackException("ConstraintViolationException");

		return ResponseEntity
			.status(HttpStatus.BAD_REQUEST)
			.body(new ErrorResponseDto(ResponseType.VALIDATION_FAILED));
	}

	// 요청 파라미터 관련 예외 처리
	@ExceptionHandler(MissingServletRequestParameterException.class)
	public ResponseEntity<ErrorResponseDto> handleMissingParameterException(MissingServletRequestParameterException e) {
		log.warn("Missing parameter: {}", e.getParameterName());
		trackException("MissingParameterException");

		return ResponseEntity
			.status(HttpStatus.BAD_REQUEST)
			.body(new ErrorResponseDto(ResponseType.VALIDATION_FAILED));
	}

	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	public ResponseEntity<ErrorResponseDto> handleTypeMismatchException(MethodArgumentTypeMismatchException e) {
		log.warn("Type mismatch for parameter '{}': {}", e.getName(), e.getValue());
		trackException("TypeMismatchException");

		return ResponseEntity
			.status(HttpStatus.BAD_REQUEST)
			.body(new ErrorResponseDto(ResponseType.VALIDATION_FAILED));
	}

	// HTTP 관련 예외 처리
	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	public ResponseEntity<ErrorResponseDto> handleMethodNotSupportedException(
		HttpRequestMethodNotSupportedException e) {
		log.warn("Method not supported: {}", e.getMethod());
		trackException("MethodNotSupportedException");

		return ResponseEntity
			.status(HttpStatus.METHOD_NOT_ALLOWED)
			.body(new ErrorResponseDto(ResponseType.VALIDATION_FAILED));
	}

	@ExceptionHandler(NoHandlerFoundException.class)
	public ResponseEntity<ErrorResponseDto> handleNoHandlerFoundException(NoHandlerFoundException e) {
		log.warn("No handler found for {} {}", e.getHttpMethod(), e.getRequestURL());
		trackException("NoHandlerFoundException");

		return ResponseEntity
			.status(HttpStatus.NOT_FOUND)
			.body(new ErrorResponseDto(ResponseType.THIS_RESOURCE_DOES_NOT_EXIST));
	}

	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<ErrorResponseDto> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
		log.warn("HTTP message not readable: {}", e.getMessage());
		trackException("HttpMessageNotReadableException");

		return ResponseEntity
			.status(HttpStatus.BAD_REQUEST)
			.body(new ErrorResponseDto(ResponseType.VALIDATION_FAILED));
	}

	@ExceptionHandler(HttpMediaTypeNotAcceptableException.class)
	public ResponseEntity<ErrorResponseDto> handleHttpMediaTypeNotAcceptableException(
		HttpMediaTypeNotAcceptableException e) {
		log.warn("HTTP media type not acceptable: {}", e.getMessage());
		trackException("HttpMediaTypeNotAcceptableException");

		return ResponseEntity
			.status(HttpStatus.NOT_ACCEPTABLE)
			.body(new ErrorResponseDto(ResponseType.VALIDATION_FAILED));
	}

	// SSE 관련 예외 처리 (정상 상황)
	@ExceptionHandler({AsyncRequestNotUsableException.class, ClientAbortException.class})
	public void handleSseException(Exception e) {
		log.debug("SSE 연결 관련 예외 (정상): {}", e.getMessage());
		// SSE 연결 끊김은 정상적인 상황이므로 응답하지 않음
	}

	@ExceptionHandler(HttpMessageNotWritableException.class)
	public void handleHttpMessageNotWritableException(HttpMessageNotWritableException e) {
		log.debug("HTTP 메시지 변환 실패 (SSE 관련): {}", e.getMessage());
		// SSE 관련 변환 실패는 응답하지 않음
	}

	// 알 수 없는 예외 처리 (Discord 알림 대상)
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponseDto> handleUnknownException(Exception e) {
		log.error("🚨 Unhandled Exception: {}", e.getMessage(), e);
		trackException("UnknownException");

		// 중요한 예외이므로 Discord 알림 발송
		notifyException(e);

		return ResponseEntity
			.status(ResponseType.DATABASE_ERROR.getHttpStatus())
			.body(new ErrorResponseDto(ResponseType.DATABASE_ERROR));
	}

	// 예외 발생 빈도 추적
	private void trackException(String exceptionType) {
		exceptionCountMap.computeIfAbsent(exceptionType, k -> new AtomicInteger(0))
			.incrementAndGet();

		// 10회마다 로그 출력
		int count = exceptionCountMap.get(exceptionType).get();
		if (count % 10 == 0) {
			log.warn("⚠️ {} 예외가 {}회 발생했습니다", exceptionType, count);
		}
	}

	// Discord 알림 발송
	private void notifyException(Exception e) {
		if (!discordWebhookUrl.isEmpty()) {
			try {
				// Discord 알림 로직은 ExceptionHandlingAspect에 이미 구현되어 있으므로
				// 여기서는 로그만 남김
				log.info("[GlobalExceptionHandler] Discord 알림 대상 예외 발생: {}", e.getClass().getSimpleName());
			} catch (Exception ex) {
				log.error("Discord 알림 발송 실패", ex);
			}
		}
	}

}

