package kernel.maidlab.core.security.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import kernel.maidlab.common.enums.UserType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;

import java.nio.charset.StandardCharsets;
import java.util.Date;

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
	public String generateAccessToken(String userId, UserType userType) {
		Date now = new Date();
		Date expiry = new Date(now.getTime() + jwtProperties.getExpiration().getAccess());

		return Jwts.builder()
			.setSubject(userId)
			.claim("userType", userType.name())
			.claim("type", "ACCESS")
			.setIssuedAt(now)
			.setExpiration(expiry)
			.signWith(getSigningKey(), SignatureAlgorithm.HS512)
			.compact();
	}

	// refresh 토큰 생성
	public String generateRefreshToken(String userId, UserType userType) {
		Date now = new Date();
		Date expiry = new Date(now.getTime() + jwtProperties.getExpiration().getRefresh());

		return Jwts.builder()
			.setSubject(userId)
			.claim("userType", userType.name())
			.claim("type", "REFRESH")
			.setIssuedAt(now)
			.setExpiration(expiry)
			.signWith(getSigningKey(), SignatureAlgorithm.HS512)
			.compact();
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
			log.error("잘못된 JWT 서명입니다.", e);
		} catch (ExpiredJwtException e) {
			log.error("만료된 JWT 토큰입니다.", e);
		} catch (UnsupportedJwtException e) {
			log.error("지원되지 않는 JWT 토큰입니다.", e);
		} catch (IllegalArgumentException e) {
			log.error("JWT 토큰이 잘못되었습니다.", e);
		}
		return false;
	}

	// 토큰으로 Spring Security Authentication 객체 생성
	public Authentication getAuthentication(String token) {
		Claims claims = parseClaims(token);

		String userId = claims.getSubject();
		String userTypeStr = claims.get("userType", String.class);

		if (userId == null || userTypeStr == null) {
			throw new IllegalArgumentException("JWT 토큰에 필수 정보가 없습니다.");
		}

		UserType userType = UserType.valueOf(userTypeStr);

		// DB 조회 없이 토큰 정보만으로 CustomUserDetails 생성
		kernel.maidlab.core.security.CustomUserDetails userDetails = kernel.maidlab.core.security.CustomUserDetails.builder()
			.userId(userId)
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

	// 사용자 id 추출
	public String getUserId(String token) {
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