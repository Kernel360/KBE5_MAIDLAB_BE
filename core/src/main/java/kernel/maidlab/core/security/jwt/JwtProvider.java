package kernel.maidlab.core.security.jwt;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import kernel.maidlab.common.enums.ResponseType;
import kernel.maidlab.common.enums.UserType;
import kernel.maidlab.core.exception.custom.AuthException;
import kernel.maidlab.core.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtProvider {

	private final JwtProperties jwtProperties;

	// 비밀 키 생성
	private SecretKey getSigningKey() {
		byte[] keyBytes = jwtProperties.getSecretKey().getBytes(StandardCharsets.UTF_8);
		return Keys.hmacShaKeyFor(keyBytes);
	}

	// access 토큰 생성
	public String generateAccessToken(String userKey, UserType userType) {
		Date now = new Date();
		Date expiry = new Date(now.getTime() + jwtProperties.getExpiration().getAccess());

		var builder = Jwts.builder()
			.setSubject(userKey)
			.claim("userType", userType.name())
			.claim("type", "ACCESS")
			.setIssuedAt(now)
			.setExpiration(expiry);

		// Admin의 경우 기존 형식과 호환되도록 role claim 추가
		if (userType == UserType.ADMIN) {
			builder.claim("role", "ADMIN");
		}

		return builder.signWith(getSigningKey(), SignatureAlgorithm.HS512).compact();
	}

	// refresh 토큰 생성
	public String generateRefreshToken(String userKey, UserType userType) {
		Date now = new Date();
		Date expiry = new Date(now.getTime() + jwtProperties.getExpiration().getRefresh());

		var builder = Jwts.builder()
			.setSubject(userKey)
			.claim("userType", userType.name())
			.claim("type", "REFRESH")
			.setIssuedAt(now)
			.setExpiration(expiry);

		// Admin의 경우 기존 형식과 호환되도록 role claim 추가
		if (userType == UserType.ADMIN) {
			builder.claim("role", "ADMIN");
		}

		return builder.signWith(getSigningKey(), SignatureAlgorithm.HS512).compact();
	}

	// 소셜 로그인 임시 토큰 생성
	public String generateTempToken(String googleId, String googleName, UserType userType) {
		Date now = new Date();
		Date expiry = new Date(now.getTime() + 300000); // 5분

		return Jwts.builder()
			.setSubject("temp_social")
			.claim("googleId", googleId)
			.claim("googleName", googleName)
			.claim("userType", userType.name())
			.claim("type", "TEMP")
			.setIssuedAt(now)
			.setExpiration(expiry)
			.signWith(getSigningKey(), SignatureAlgorithm.HS512)
			.compact();
	}

	// 토큰 유효성 검증
	public boolean validateToken(String token) {
		try {
			Jwts.parserBuilder()
				.setSigningKey(getSigningKey())
				.build()
				.parseClaimsJws(token);
			return true;
		} catch (SecurityException | MalformedJwtException e) {
			log.warn("잘못된 JWT 서명입니다: {}", e.getMessage());
		} catch (ExpiredJwtException e) {
			log.warn("만료된 JWT 토큰입니다: {}", e.getMessage());
		} catch (UnsupportedJwtException e) {
			log.warn("지원되지 않는 JWT 토큰입니다: {}", e.getMessage());
		} catch (IllegalArgumentException e) {
			log.warn("JWT 토큰이 잘못되었습니다: {}", e.getMessage());
		}
		return false;
	}

	// 토큰으로 Spring Security Authentication 객체 생성
	public Authentication getAuthentication(String token) {
		Claims claims = parseClaims(token);

		String userKey = claims.getSubject();
		String userTypeStr = claims.get("userType", String.class);

		if (userKey == null || userTypeStr == null) {
			throw AuthException.builder(ResponseType.AUTHORIZATION_FAILED)
				.message("JWT 토큰에 필수 정보가 없습니다")
				.build();
		}

		UserType userType = UserType.valueOf(userTypeStr);

		// DB 조회 없이 토큰 정보만으로 CustomUserDetails 생성
		CustomUserDetails userDetails = CustomUserDetails.builder()
			.userKey(userKey)
			.userType(userType)
			.build();

		return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
	}

	// claims 추출
	public Claims parseClaims(String token) {
		try {
			return Jwts.parserBuilder()
				.setSigningKey(getSigningKey())
				.build()
				.parseClaimsJws(token)
				.getBody();
		} catch (ExpiredJwtException e) {
			log.warn("만료된 JWT 토큰에서 Claims 추출: {}", e.getMessage());
			return e.getClaims();
		}
	}

	// 사용자 key 추출
	public String getUserKey(String token) {
		return parseClaims(token).getSubject();
	}

	// 사용자 타입 추출
	public UserType getUserType(String token) {
		String userTypeStr = parseClaims(token).get("userType", String.class);
		return UserType.valueOf(userTypeStr);
	}

	// 토큰 타입 확인
	public String getTokenType(String token) {
		return parseClaims(token).get("type", String.class);
	}

	// access 확인
	public boolean isAccessToken(String token) {
		return "ACCESS".equals(getTokenType(token));
	}

	// refresh 확인
	public boolean isRefreshToken(String token) {
		return "REFRESH".equals(getTokenType(token));
	}

	// refresh 토큰이 아닌지 확인
	public boolean isNotRefreshToken(String token) {
		return !isRefreshToken(token);
	}

	// temp 토큰 확인
	public boolean isTempToken(String token) {
		return "TEMP".equals(getTokenType(token));
	}

	// 토큰 추출 (HttpServletRequest 에서)
	public String extractToken(jakarta.servlet.http.HttpServletRequest request) {
		String authHeader = request.getHeader(jwtProperties.getHeader());

		if (authHeader != null && authHeader.startsWith(jwtProperties.getPrefix())) {
			return authHeader.substring(jwtProperties.getPrefix().length());
		}

		return null;
	}
}