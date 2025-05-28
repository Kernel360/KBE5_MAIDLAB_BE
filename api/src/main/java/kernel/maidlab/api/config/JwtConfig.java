package kernel.maidlab.api.config;

import org.springframework.context.annotation.Configuration;

import jakarta.servlet.http.HttpServletRequest;

import kernel.maidlab.api.auth.entity.Consumer;
import kernel.maidlab.api.auth.entity.Manager;
import kernel.maidlab.api.exception.BaseException;
import kernel.maidlab.common.enums.UserType;
import kernel.maidlab.common.enums.ResponseType;
import kernel.maidlab.api.auth.jwt.JwtFilter;

@Configuration
public class JwtConfig {

	public static boolean isNotAuthenticated(HttpServletRequest request) {
		return request.getAttribute(JwtFilter.CURRENT_USER_KEY) == null;
	}

	public static String getCurrentUserUuid(HttpServletRequest request) {
		return (String)request.getAttribute(JwtFilter.CURRENT_USER_UUID_KEY);
	}

	public static UserType getCurrentUserType(HttpServletRequest request) {
		return (UserType)request.getAttribute(JwtFilter.CURRENT_USER_TYPE_KEY);
	}

	public static Consumer getCurrentConsumer(HttpServletRequest request) {
		Object user = request.getAttribute(JwtFilter.CURRENT_USER_KEY);
		return (user instanceof Consumer) ? (Consumer)user : null;
	}

	public static Manager getCurrentManager(HttpServletRequest request) {
		Object user = request.getAttribute(JwtFilter.CURRENT_USER_KEY);
		return (user instanceof Manager) ? (Manager)user : null;
	}

	public static void checkConsumerAuth(HttpServletRequest request) {
		if (isNotAuthenticated(request)) {
			throw new BaseException(ResponseType.AUTHORIZATION_FAILED);
		}
		if (getCurrentConsumer(request) == null) {
			throw new BaseException(ResponseType.DO_NOT_HAVE_PERMISSION);
		}
	}

	public static void checkManagerAuth(HttpServletRequest request) {
		if (isNotAuthenticated(request)) {
			throw new BaseException(ResponseType.AUTHORIZATION_FAILED);
		}
		if (getCurrentManager(request) == null) {
			throw new BaseException(ResponseType.DO_NOT_HAVE_PERMISSION);
		}
	}
}
