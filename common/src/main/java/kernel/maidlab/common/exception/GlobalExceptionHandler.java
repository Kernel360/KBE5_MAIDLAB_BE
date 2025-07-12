package kernel.maidlab.common.exception;

import kernel.maidlab.common.dto.ErrorResponseDto;
import kernel.maidlab.common.enums.ResponseType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.async.AsyncRequestNotUsableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.apache.catalina.connector.ClientAbortException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
	@ExceptionHandler(BaseException.class)
	public ResponseEntity<ErrorResponseDto> handleBaseException(BaseException e) {
		ResponseType type = e.getResponseType();
		log.error("Handled BaseException - code: {}, message: {}", type.getCode(), type.getMessage());
		return ResponseEntity
			.status(type.getHttpStatus())
			.body(new ErrorResponseDto(type));
	}

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

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponseDto> handleUnknownException(Exception e) {
		log.error("Unhandled Exception: {}", e.getMessage(), e);
		return ResponseEntity
			.status(ResponseType.DATABASE_ERROR.getHttpStatus())
			.body(new ErrorResponseDto(ResponseType.DATABASE_ERROR));
	}
}


