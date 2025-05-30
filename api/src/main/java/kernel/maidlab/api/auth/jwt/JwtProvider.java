package kernel.maidlab.api.auth.jwt;

import jakarta.servlet.http.HttpServletRequest;

import kernel.maidlab.api.auth.entity.Consumer;
import kernel.maidlab.api.auth.entity.Manager;
import kernel.maidlab.api.auth.repository.ConsumerRepository;
import kernel.maidlab.api.auth.repository.ManagerRepository;
import kernel.maidlab.common.enums.UserType;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.crypto.SecretKey;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.UUID;

@Component
public class JwtProvider {

	private static final Logger log = LoggerFactory.getLogger(JwtProvider.class);

	private final JwtProperties jwtProperties;
	private final ConsumerRepository consumerRepository;
	private final ManagerRepository managerRepository;

	public JwtProvider(JwtProperties jwtProperties, ConsumerRepository consumerRepository,
		ManagerRepository managerRepository) {
		this.jwtProperties = jwtProperties;
		this.consumerRepository = consumerRepository;
		this.managerRepository = managerRepository;
	}

	@Transactional
	public JwtDto.TokenPair generateTokenPair(String uuid, UserType userType) {
		String accessToken = generateAccessToken(uuid, userType);
		String refreshToken = generateRefreshToken(uuid, userType);

		saveRefreshToken(uuid, userType, refreshToken);

		return new JwtDto.TokenPair(accessToken, refreshToken);
	}

	// 엑세스 토큰 생성
	public String generateAccessToken(String uuid, UserType userType) {
		Date now = new Date();
		Date expiryDate = new Date(now.getTime() + jwtProperties.getExpiration().getAccess());

		return Jwts.builder()
			.setSubject(uuid)
			.claim("userType", userType.name())
			.claim("type", "access")
			.claim("tokenId", UUID.randomUUID().toString())
			.setIssuedAt(now)
			.setExpiration(expiryDate)
			.signWith(getSignKey(), SignatureAlgorithm.HS512)
			.compact();
	}

	// 리프레시 토큰 생성
	public String generateRefreshToken(String uuid, UserType userType) {
		Date now = new Date();
		Date expiryDate = new Date(now.getTime() + jwtProperties.getExpiration().getRefresh());

		return Jwts.builder()
			.setSubject(uuid)
			.claim("userType", userType.name())
			.claim("type", "refresh")
			.claim("tokenId", UUID.randomUUID().toString())
			.setIssuedAt(now)
			.setExpiration(expiryDate)
			.signWith(getSignKey(), SignatureAlgorithm.HS512)
			.compact();
	}

	// 임시 토큰 생성
	public String generateTempToken(String googleId, String googleName, UserType userType) {
		Date now = new Date();
		Date expiryDate = new Date(now.getTime() + 300000);

		return Jwts.builder()
			.setSubject("temp_social")
			.claim("googleId", googleId)
			.claim("googleName", googleName)
			.claim("userType", userType.name())
			.claim("type", "temp")
			.setIssuedAt(now)
			.setExpiration(expiryDate)
			.signWith(getSignKey(), SignatureAlgorithm.HS512)
			.compact();
	}

	// 토큰 갱신
	@Transactional
	public JwtDto.RefreshResult refreshTokens(String refreshToken) {
		JwtDto.ValidationResult validationResult = validateRefreshToken(refreshToken);
		if (!validationResult.isValid()) {
			return JwtDto.RefreshResult.failure("유효하지 않은 refresh token");
		}

		String uuid = validationResult.getUuid();
		UserType userType = validationResult.getUserType();

		String storedRefreshToken = getStoredRefreshToken(uuid, userType);
		if (storedRefreshToken == null || !storedRefreshToken.equals(refreshToken)) {
			return JwtDto.RefreshResult.failure("저장된 refresh token과 불일치");
		}

		JwtDto.TokenPair tokenPair = generateTokenPair(uuid, userType);

		return JwtDto.RefreshResult.success(tokenPair.getAccessToken(), tokenPair.getRefreshToken());
	}

	// 토큰 추출
	public String extractToken(HttpServletRequest request) {
		String authHeader = request.getHeader(jwtProperties.getHeader());

		if (authHeader != null && authHeader.startsWith(jwtProperties.getPrefix())) {
			return authHeader.substring(jwtProperties.getPrefix().length());
		}

		return null;
	}

	// Access 토큰 검증 및 파싱
	public JwtDto.ValidationResult validateAccessToken(String token) {
		return validateToken(token, "access");
	}

	// Refresh 토큰 검증 및 파싱
	public JwtDto.ValidationResult validateRefreshToken(String token) {
		return validateToken(token, "refresh");
	}

	// 토큰 검증 및 파싱
	public JwtDto.ValidationResult validateToken(String token, String type) {
		if (token == null || token.trim().isEmpty()) {
			return JwtDto.ValidationResult.failure("토큰이 비어있습니다.");
		}

		try {
			Claims claims = Jwts.parserBuilder()
				.setSigningKey(getSignKey())
				.build()
				.parseClaimsJws(token)
				.getBody();

			String uuid = claims.getSubject();
			String userTypeString = (String)claims.get("userType");
			String tokenType = (String)claims.get("type");

			if (uuid == null || userTypeString == null || tokenType == null) {
				return JwtDto.ValidationResult.failure("토큰에 정보가 없습니다.");
			}

			if (!type.equals(tokenType)) {
				return JwtDto.ValidationResult.failure("토큰 타입이 일치하지 않습니다.");
			}

			UserType usertype = UserType.valueOf(userTypeString);

			return JwtDto.ValidationResult.success(uuid, usertype);

		} catch (Exception e) {
			return JwtDto.ValidationResult.failure("토큰 검증 실패");
		}
	}

	// 임시 토큰 검증 및 파싱
	public JwtDto.TempTokenInfo validateTempToken(String token) {
		if (token == null || token.trim().isEmpty()) {
			return JwtDto.TempTokenInfo.failure("토큰이 비어있습니다.");
		}

		try {
			Claims claims = Jwts.parserBuilder()
				.setSigningKey(getSignKey())
				.build()
				.parseClaimsJws(token)
				.getBody();

			String type = (String) claims.get("type");
			if (!"temp".equals(type)) {
				return JwtDto.TempTokenInfo.failure("임시 토큰이 아닙니다.");
			}

			String googleId = (String) claims.get("googleId");
			String googleName = (String) claims.get("googleName");
			String userTypeStr = (String) claims.get("userType");

			if (googleId == null || googleName == null || userTypeStr == null) {
				return JwtDto.TempTokenInfo.failure("토큰에 필요한 정보가 없습니다.");
			}

			UserType userType = UserType.valueOf(userTypeStr);
			return JwtDto.TempTokenInfo.success(googleId, googleName, userType);

		} catch (Exception e) {
			return JwtDto.TempTokenInfo.failure("토큰 검증 실패");
		}
	}

	// 유저 찾기
	public Object findUser(String uuid, UserType userType) {
		try {
			if (userType == UserType.CONSUMER) {
				return consumerRepository.findByUuid(uuid).orElse(null);
			} else if (userType == UserType.MANAGER) {
				return managerRepository.findByUuid(uuid).orElse(null);
			}
		} catch (Exception e) {
			log.error("사용자 조회 중 오류", e);
		}
		return null;
	}

	// DB에 refresh token 저장
	@Transactional
	public void saveRefreshToken(String uuid, UserType userType, String refreshToken) {
		try {
			if (userType == UserType.CONSUMER) {
				Consumer consumer = consumerRepository.findByUuid(uuid).orElse(null);
				if (consumer != null) {
					consumer.updateRefreshToken(refreshToken);
					consumerRepository.save(consumer);
				}
			} else if (userType == UserType.MANAGER) {
				Manager manager = managerRepository.findByUuid(uuid).orElse(null);
				if (manager != null) {
					manager.updateRefreshToken(refreshToken);
					managerRepository.save(manager);
				}
			}
		} catch (Exception e) {
			log.error("refresh token 저장 중 오류", e);
			throw new RuntimeException("refresh token 저장 실패", e);
		}
	}

	// DB에서 저장된 refresh token 조회
	public String getStoredRefreshToken(String uuid, UserType userType) {
		try {
			if (userType == UserType.CONSUMER) {
				return consumerRepository.findByUuid(uuid)
					.map(Consumer::getRefreshToken)
					.orElse(null);
			} else if (userType == UserType.MANAGER) {
				return managerRepository.findByUuid(uuid)
					.map(Manager::getRefreshToken)
					.orElse(null);
			}
		} catch (Exception e) {
			log.error("저장된 refresh token 조회 중 오류", e);
		}
		return null;
	}

	// 로그아웃 시 refresh token 삭제
	@Transactional
	public void removeRefreshToken(String uuid, UserType userType) {
		saveRefreshToken(uuid, userType, null);
	}

	// 서명 키 생성
	private SecretKey getSignKey() {
		byte[] keyBytes = jwtProperties.getSecretKey().getBytes(StandardCharsets.UTF_8);
		return Keys.hmacShaKeyFor(keyBytes);
	}
}