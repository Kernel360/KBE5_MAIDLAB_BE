package kernel.maidlab.core.security.jwt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import kernel.maidlab.common.enums.UserType;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AdminJwtProvider {

	private static final Logger log = LoggerFactory.getLogger(AdminJwtProvider.class);

	private final JwtProvider jwtProvider;

	// 관리자용 액세스 토큰 생성
	public String generateAdminAccessToken(String adminKey) {
		return jwtProvider.generateAccessToken(adminKey, UserType.ADMIN);
	}

	// 관리자용 리프레시 토큰 생성
	public String generateAdminRefreshToken(String adminKey) {
		return jwtProvider.generateRefreshToken(adminKey, UserType.ADMIN);
	}

	// 관리자용 토큰 검증 (통합 JWT Provider 사용)
	public boolean validateAdminToken(String token, String type) {
		if (token == null || token.trim().isEmpty()) {
			return false;
		}

		try {
			// 통합 JWT Provider로 토큰 검증
			if (!jwtProvider.validateToken(token)) {
				return false;
			}

			UserType userType = jwtProvider.getUserType(token);
			if (userType != UserType.ADMIN) {
				return false;
			}

			// 토큰 타입 검증 (ACCESS/REFRESH vs access/refresh 호환)
			boolean typeMatches = switch (type.toLowerCase()) {
				case "access" -> jwtProvider.isAccessToken(token);
				case "refresh" -> jwtProvider.isRefreshToken(token);
				default -> false;
			};

			return typeMatches;

		} catch (Exception e) {
			log.error("관리자 토큰 검증 실패", e);
			return false;
		}
	}

	// Access 토큰 검증
	public boolean validateAdminAccessToken(String token) {
		return validateAdminToken(token, "access");
	}

	// Refresh 토큰 검증
	public boolean validateAdminRefreshToken(String token) {
		return validateAdminToken(token, "refresh");
	}

	// 관리자 키 추출
	public String getAdminKey(String token) {
		return jwtProvider.getUserId(token);
	}
}