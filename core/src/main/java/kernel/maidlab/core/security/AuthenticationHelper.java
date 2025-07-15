package kernel.maidlab.core.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;
import kernel.maidlab.common.enums.ResponseType;
import kernel.maidlab.common.enums.UserType;
import kernel.maidlab.core.exception.custom.AuthException;

public class AuthenticationHelper {

	// 현재 인증된 사용자 정보 반환
	public static CustomUserDetails getCurrentUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		if (authentication == null || !authentication.isAuthenticated()) {
			throw AuthException.builder(ResponseType.AUTHORIZATION_FAILED)
				.message("인증되지 않은 사용자입니다")
				.clientIp(getClientIp())
				.attemptedAction("getCurrentUser")
				.build();
		}

		if (authentication.getPrincipal().equals("anonymousUser")) {
			throw AuthException.builder(ResponseType.AUTHORIZATION_FAILED)
				.message("익명 사용자는 접근할 수 없습니다")
				.clientIp(getClientIp())
				.attemptedAction("getCurrentUser")
				.build();
		}

		if (!(authentication.getPrincipal() instanceof CustomUserDetails customUserDetails)) {
			throw AuthException.builder(ResponseType.AUTHORIZATION_FAILED)
				.message("잘못된 사용자 인증 정보입니다")
				.clientIp(getClientIp())
				.attemptedAction("getCurrentUser")
				.build();
		}

		return customUserDetails;
	}

	// 사용자 uuid or adminKey 반환
	public static String getCurrentUserKey() {
		return getCurrentUser().getUserKey();
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

	// 클라이언트 IP 주소 추출
	private static String getClientIp() {
		try {
			ServletRequestAttributes attributes = (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();
			if (attributes == null) {
				return "unknown";
			}

			HttpServletRequest request = attributes.getRequest();

			// X-Forwarded-For 헤더 확인 (프록시 서버를 통한 요청)
			String xForwardedFor = request.getHeader("X-Forwarded-For");
			if (xForwardedFor != null && !xForwardedFor.isEmpty() && !"unknown".equalsIgnoreCase(xForwardedFor)) {
				return xForwardedFor.split(",")[0].trim();
			}

			// X-Real-IP 헤더 확인
			String xRealIp = request.getHeader("X-Real-IP");
			if (xRealIp != null && !xRealIp.isEmpty() && !"unknown".equalsIgnoreCase(xRealIp)) {
				return xRealIp;
			}

			// 기본 RemoteAddr 사용
			return request.getRemoteAddr();
		} catch (Exception e) {
			return "unknown";
		}
	}

}