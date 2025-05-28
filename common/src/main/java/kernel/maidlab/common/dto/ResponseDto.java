package kernel.maidlab.common.dto;

import java.io.IOException;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletResponse;
import kernel.maidlab.common.enums.ResponseType;
import lombok.AllArgsConstructor;
import lombok.Getter;

import org.springframework.http.ResponseEntity;

@Getter
@AllArgsConstructor
public class ResponseDto<T> {

	@JsonIgnore
	private final ResponseType responseType;
	private final T data;

	@JsonProperty("code")
	public String getCode() {
		return responseType.getCode();
	}

	@JsonProperty("message")
	public String getMessage() {
		return responseType.getMessage();
	}

	public static <T> ResponseEntity<ResponseDto<T>> success(ResponseType responseType, T data) {
		return ResponseEntity
			.status(ResponseType.SUCCESS.getHttpStatus())
			.body(new ResponseDto<>(responseType, data));
	}

	public static <T> ResponseEntity<ResponseDto<T>> dataBaseError(ResponseType responseType) {
		return ResponseEntity
			.status(ResponseType.DATABASE_ERROR.getHttpStatus())
			.body(new ResponseDto<>(responseType, null));
	}

	public static void sendErrorResponse(HttpServletResponse response, ResponseType responseType,
		ObjectMapper objectMapper) throws IOException {
		ResponseDto<Object> errorResponse = new ResponseDto<>(responseType, null);

		response.setStatus(responseType.getHttpStatus().value());
		response.setContentType("application/json;charset=UTF-8");

		response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
	}

	public static void authorizationFailed(HttpServletResponse response, ObjectMapper objectMapper) throws
		IOException {
		sendErrorResponse(response, ResponseType.AUTHORIZATION_FAILED, objectMapper);
	}

	public static void doNotHavePermission(HttpServletResponse response, ObjectMapper objectMapper) throws
		IOException {
		sendErrorResponse(response, ResponseType.DO_NOT_HAVE_PERMISSION, objectMapper);
	}

	public static void validationFailed(HttpServletResponse response, ObjectMapper objectMapper) throws
		IOException {
		sendErrorResponse(response, ResponseType.VALIDATION_FAILED, objectMapper);
	}
}
