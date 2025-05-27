package kernel.maidlab.common.jwt;

import jakarta.servlet.http.HttpServletRequest;
import kernel.maidlab.api.auth.repository.ConsumerRepository;
import kernel.maidlab.api.auth.repository.ManagerRepository;
import kernel.maidlab.common.dto.JwtDto;
import kernel.maidlab.common.enums.UserType;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;

import java.nio.charset.StandardCharsets;

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

	// 토큰 추출
	public String extractToken(HttpServletRequest request) {
		String authHeader = request.getHeader(jwtProperties.getHeader());

		if (authHeader != null && authHeader.startsWith(jwtProperties.getPrefix())) {
			return authHeader.substring(jwtProperties.getPrefix().length());
		}

		return null;
	}

	// 토큰 검증 및 파싱
	public JwtDto.ValidationResult validateToken(String token) {
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

			if (uuid == null || userTypeString == null) {
				return JwtDto.ValidationResult.failure("토큰에 정보가 없습니다.");
			}

			UserType usertype = UserType.valueOf(userTypeString);

			return JwtDto.ValidationResult.success(uuid, usertype);

		} catch (Exception e) {
			return JwtDto.ValidationResult.failure("토큰 검증 실패");
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

	// 서명 키 생성
	private SecretKey getSignKey() {
		byte[] keyBytes = jwtProperties.getSecretKey().getBytes(StandardCharsets.UTF_8);
		return Keys.hmacShaKeyFor(keyBytes);
	}
}