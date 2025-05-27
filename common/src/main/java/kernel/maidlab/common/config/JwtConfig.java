package kernel.maidlab.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;

import jakarta.servlet.http.HttpServletRequest;
import kernel.maidlab.api.auth.entity.Consumer;
import kernel.maidlab.api.auth.entity.Manager;
import kernel.maidlab.common.dto.ResponseDto;
import kernel.maidlab.common.enums.UserType;
import kernel.maidlab.common.enums.ResponseType;
import kernel.maidlab.common.jwt.JwtFilter;

@Configuration
public class JwtConfig {

	public static boolean isNotAuthenticated(HttpServletRequest request) {
		return request.getAttribute(JwtFilter.CURRENT_USER_KEY) != null;
	}

	public static String getCurrentUserUuid(HttpServletRequest request) {
		return (String) request.getAttribute(JwtFilter.CURRENT_USER_UUID_KEY);
	}

	public static UserType getCurrentUserType(HttpServletRequest request) {
		return (UserType) request.getAttribute(JwtFilter.CURRENT_USER_TYPE_KEY);
	}

	public static Consumer getCurrentConsumer(HttpServletRequest request) {
		Object user = request.getAttribute(JwtFilter.CURRENT_USER_KEY);
		return (user instanceof Consumer) ? (Consumer) user : null;
	}

	public static Manager getCurrentManager(HttpServletRequest request) {
		Object user = request.getAttribute(JwtFilter.CURRENT_USER_KEY);
		return (user instanceof Manager) ? (Manager) user : null;
	}

	public static <T> ResponseEntity<ResponseDto<T>> checkConsumerAuth(HttpServletRequest request) {
		if (isNotAuthenticated(request)) {
			return ResponseDto.dataBaseError(ResponseType.AUTHORIZATION_FAILED);
		}

		if (getCurrentConsumer(request) == null) {
			return ResponseDto.dataBaseError(ResponseType.DO_NOT_HAVE_PERMISSION);
		}

		return null;
	}

	public static <T> ResponseEntity<ResponseDto<T>> checkManagerAuth(HttpServletRequest request) {
		if (isNotAuthenticated(request)) {
			return ResponseDto.dataBaseError(ResponseType.AUTHORIZATION_FAILED);
		}

		if (getCurrentManager(request) == null) {
			return ResponseDto.dataBaseError(ResponseType.DO_NOT_HAVE_PERMISSION);
		}

		return null;
	}
}
