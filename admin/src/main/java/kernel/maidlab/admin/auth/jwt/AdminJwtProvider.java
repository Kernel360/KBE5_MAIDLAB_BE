package kernel.maidlab.admin.auth.jwt;

import kernel.maidlab.admin.auth.entity.Admin;
import kernel.maidlab.admin.auth.repository.AdminRepository;
import kernel.maidlab.api.auth.jwt.JwtProperties;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.crypto.SecretKey;

import jakarta.servlet.http.HttpServletRequest;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.UUID;

@Component
public class AdminJwtProvider {

	private static final Logger log = LoggerFactory.getLogger(AdminJwtProvider.class);

	private final JwtProperties jwtProperties;
	private final AdminRepository adminRepository;

	public AdminJwtProvider(JwtProperties jwtProperties, AdminRepository adminRepository) {
		this.jwtProperties = jwtProperties;
		this.adminRepository = adminRepository;
	}

	@Transactional
	public AdminJwtDto.TokenPair generateAdminTokenPair(String adminKey) {
		String accessToken = generateAdminAccessToken(adminKey);
		String refreshToken = generateAdminRefreshToken(adminKey);

		saveAdminRefreshToken(adminKey, refreshToken);

		return new AdminJwtDto.TokenPair(accessToken, refreshToken);
	}

	// 관리자용 액세스 토큰 생성
	public String generateAdminAccessToken(String adminKey) {
		Date now = new Date();
		Date expiryDate = new Date(now.getTime() + jwtProperties.getExpiration().getAccess());

		return Jwts.builder()
			.setSubject(adminKey)
			.claim("role", "ADMIN")
			.claim("type", "access")
			.claim("tokenId", UUID.randomUUID().toString())
			.setIssuedAt(now)
			.setExpiration(expiryDate)
			.signWith(getSignKey(), SignatureAlgorithm.HS512)
			.compact();
	}

	// 관리자용 리프레시 토큰 생성
	public String generateAdminRefreshToken(String adminKey) {
		Date now = new Date();
		Date expiryDate = new Date(now.getTime() + jwtProperties.getExpiration().getRefresh());

		return Jwts.builder()
			.setSubject(adminKey)
			.claim("role", "ADMIN")
			.claim("type", "refresh")
			.claim("tokenId", UUID.randomUUID().toString())
			.setIssuedAt(now)
			.setExpiration(expiryDate)
			.signWith(getSignKey(), SignatureAlgorithm.HS512)
			.compact();
	}

	// 관리자 토큰 갱신
	@Transactional
	public AdminJwtDto.AdminRefreshResult refreshAdminTokens(String refreshToken) {
		AdminJwtDto.AdminValidationResult validationResult = validateAdminToken(refreshToken, "refresh");
		if (!validationResult.isValid()) {
			return AdminJwtDto.AdminRefreshResult.failure("유효하지 않은 refresh token");
		}

		String adminKey = validationResult.getAdminKey();

		String storedRefreshToken = getStoredAdminRefreshToken(adminKey);
		if (storedRefreshToken == null || !storedRefreshToken.equals(refreshToken)) {
			return AdminJwtDto.AdminRefreshResult.failure("저장된 refresh token과 불일치");
		}

		AdminJwtDto.TokenPair tokenPair = generateAdminTokenPair(adminKey);

		return AdminJwtDto.AdminRefreshResult.success(tokenPair.getAccessToken(), tokenPair.getRefreshToken());
	}

	// 토큰 추출
	public String extractToken(HttpServletRequest request) {
		String authHeader = request.getHeader(jwtProperties.getHeader());

		if (authHeader != null && authHeader.startsWith(jwtProperties.getPrefix())) {
			return authHeader.substring(jwtProperties.getPrefix().length());
		}

		return null;
	}

	// 관리자용 토큰 검증
	public AdminJwtDto.AdminValidationResult validateAdminToken(String token, String type) {
		if (token == null || token.trim().isEmpty()) {
			return AdminJwtDto.AdminValidationResult.failure("토큰이 비어있습니다.");
		}

		try {
			Claims claims = Jwts.parserBuilder()
				.setSigningKey(getSignKey())
				.build()
				.parseClaimsJws(token)
				.getBody();

			String adminKey = claims.getSubject();
			String role = (String)claims.get("role");
			String tokenType = (String)claims.get("type");

			if (adminKey == null || role == null || tokenType == null) {
				return AdminJwtDto.AdminValidationResult.failure("토큰에 정보가 없습니다.");
			}

			if (!type.equals(tokenType)) {
				return AdminJwtDto.AdminValidationResult.failure("토큰 타입이 일치하지 않습니다.");
			}

			if (!"ADMIN".equals(role)) {
				return AdminJwtDto.AdminValidationResult.failure("관리자 토큰이 아닙니다.");
			}

			return AdminJwtDto.AdminValidationResult.success(adminKey);

		} catch (Exception e) {
			return AdminJwtDto.AdminValidationResult.failure("토큰 검증 실패");
		}
	}

	// Access 토큰 검증
	public AdminJwtDto.AdminValidationResult validateAdminAccessToken(String token) {
		return validateAdminToken(token, "access");
	}

	// 관리자 찾기
	public Admin findAdmin(String adminKey) {
		try {
			return adminRepository.findByAdminKeyAndIsDeletedFalse(adminKey).orElse(null);
		} catch (Exception e) {
			log.error("관리자 조회 중 오류", e);
		}
		return null;
	}

	// 관리자용 리프레시 토큰 저장
	@Transactional
	public void saveAdminRefreshToken(String adminKey, String refreshToken) {
		try {
			Admin admin = adminRepository.findByAdminKey(adminKey).orElse(null);
			if (admin != null) {
				admin.updateRefreshToken(refreshToken);
				adminRepository.save(admin);
			}
		} catch (Exception e) {
			log.error("관리자 refresh token 저장 중 오류", e);
			throw new RuntimeException("관리자 refresh token 저장 실패", e);
		}
	}

	// 관리자 저장된 리프레시 토큰 조회
	public String getStoredAdminRefreshToken(String adminKey) {
		try {
			return adminRepository.findByAdminKey(adminKey)
				.map(Admin::getRefreshToken)
				.orElse(null);
		} catch (Exception e) {
			log.error("관리자 저장된 refresh token 조회 중 오류", e);
		}
		return null;
	}

	// 관리자 리프레시 토큰 삭제
	@Transactional
	public void removeAdminRefreshToken(String adminKey) {
		saveAdminRefreshToken(adminKey, null);
	}

	// 서명 키 생성
	private SecretKey getSignKey() {
		byte[] keyBytes = jwtProperties.getSecretKey().getBytes(StandardCharsets.UTF_8);
		return Keys.hmacShaKeyFor(keyBytes);
	}
}