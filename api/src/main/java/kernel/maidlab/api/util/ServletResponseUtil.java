package kernel.maidlab.api.util;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletResponse;
import kernel.maidlab.api.exception.dto.ErrorResponseDto;
import kernel.maidlab.common.enums.ResponseType;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
public class ServletResponseUtil {

	public static void sendErrorResponse(HttpServletResponse response, ResponseType responseType,
		ObjectMapper objectMapper) throws IOException {
		ErrorResponseDto errorResponse = new ErrorResponseDto(responseType);

		response.setStatus(responseType.getHttpStatus().value());
		response.setContentType("application/json;charset=UTF-8");

		response.getWriter().write(objectMapper.writeValueAsString(errorResponse));

		log.debug("Sent error response - code: {}, message: {}", responseType.getCode(), responseType.getMessage());
	}

	public static void authorizationFailed(HttpServletResponse response, ObjectMapper objectMapper) throws IOException {
		sendErrorResponse(response, ResponseType.AUTHORIZATION_FAILED, objectMapper);
	}

	public static void doNotHavePermission(HttpServletResponse response, ObjectMapper objectMapper) throws IOException {
		sendErrorResponse(response, ResponseType.DO_NOT_HAVE_PERMISSION, objectMapper);
	}
}