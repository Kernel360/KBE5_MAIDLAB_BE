package kernel.maidlab.api.exception;

import kernel.maidlab.api.exception.dto.ErrorResponseDto;
import kernel.maidlab.common.enums.ResponseType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

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

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponseDto> handleUnknownException(Exception e) {
		log.error("Unhandled Exception: {}", e.getMessage(), e);
		return ResponseEntity
			.status(ResponseType.DATABASE_ERROR.getHttpStatus())
			.body(new ErrorResponseDto(ResponseType.DATABASE_ERROR));
	}
}


