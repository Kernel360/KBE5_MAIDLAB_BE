package kernel.maidlab.core.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import kernel.maidlab.common.enums.ResponseType;
import kernel.maidlab.common.enums.UserType;
import kernel.maidlab.common.exception.custom.AuthException;

public class AuthenticationHelper {

	// 현재 인증된 사용자 정보 반환
	public static CustomUserDetails getCurrentUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		if (authentication == null || !authentication.isAuthenticated()) {
			throw new AuthException(ResponseType.AUTHORIZATION_FAILED);
		}

		if (authentication.getPrincipal().equals("anonymousUser")) {
			throw new AuthException(ResponseType.AUTHORIZATION_FAILED);
		}

		if (!(authentication.getPrincipal() instanceof CustomUserDetails)) {
			throw new AuthException(ResponseType.AUTHORIZATION_FAILED);
		}

		return (CustomUserDetails)authentication.getPrincipal();
	}

	// 사용자 uuid or adminKey 반환
	public static String getCurrentUserId() {
		return getCurrentUser().getUserId();
	}

	// 사용자 타입 변환
	public static UserType getCurrentUserType() {
		return getCurrentUser().getUserType();
	}

	// 권한 확인
	public static boolean hasRole(UserType requiredType) {
		try {
			return getCurrentUser().hasRole(requiredType);
		} catch (AuthException e) {
			return false;
		}
	}

	// 관리자 확인
	public static boolean isAdmin() {
		return hasRole(UserType.ADMIN);
	}

	// 소비자 확인
	public static boolean isConsumer() {
		return hasRole(UserType.CONSUMER);
	}

	// 매니저 확인
	public static boolean isManager() {
		return hasRole(UserType.MANAGER);
	}

}