package kernel.maidlab.core.security.filter;

import java.io.IOException;

import org.springframework.lang.NonNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kernel.maidlab.core.security.jwt.JwtProperties;
import kernel.maidlab.core.security.jwt.JwtProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	private final JwtProvider jwtProvider;
	private final JwtProperties jwtProperties;

	@Override
	protected void doFilterInternal(@NonNull HttpServletRequest request,
		@NonNull HttpServletResponse response,
		@NonNull FilterChain filterChain) throws ServletException, IOException {

		try {
			String jwt = extractTokenFromRequest(request);

			if (jwt != null && jwtProvider.validateToken(jwt)) {

				if (jwtProvider.isAccessToken(jwt)) {
					Authentication authentication = jwtProvider.getAuthentication(jwt);

					SecurityContextHolder.getContext().setAuthentication(authentication);

					log.debug("JWT 토큰 인증 성공 - 사용자: {}, 타입: {}",
						jwtProvider.getUserKey(jwt),
						jwtProvider.getUserType(jwt));
				} else {
					log.warn("Access Token이 아닌 토큰으로 인증 시도: {}", jwtProvider.getTokenType(jwt));
				}
			} else if (jwt != null) {
				log.warn("유효하지 않은 JWT 토큰입니다.");
			}

		} catch (Exception e) {
			log.error("JWT 토큰 인증 처리 중 오류 발생", e);
			SecurityContextHolder.clearContext();
		}

		filterChain.doFilter(request, response);
	}

	// http request 의 jwt 토큰 추출
	private String extractTokenFromRequest(HttpServletRequest request) {
		// 1. 헤더에서 토큰 확인
		String bearerToken = request.getHeader(jwtProperties.getHeader());
		if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(jwtProperties.getPrefix())) {
			return bearerToken.substring(jwtProperties.getPrefix().length()).trim();
		}

		// 2. URL 파라미터에서 토큰 확인 (SSE 연결용)
		String tokenParam = request.getParameter("token");
		if (StringUtils.hasText(tokenParam)) {
			return tokenParam;
		}

		return null;
	}

	// path 필터
	@Override
	protected boolean shouldNotFilter(@NonNull HttpServletRequest request) throws ServletException {
		String path = request.getRequestURI();

		return isPublicPath(path);
	}

	// path 관리
	private boolean isPublicPath(String path) {

		// 공개 API 경로 목록
		String[] publicPaths = {
			"/api/auth/login",
			"/api/auth/signup",
			"/api/auth/social-login",
			"/api/auth/social-signup",
			"/api/admin/auth/login",
			"/api/events",
			"/api/events/**",
			"/swagger-ui",
			"/v3/api-docs",
			"/swagger-resources",
			"/webjars",
			"/favicon.ico",
			"/error"
		};

		for (String publicPath : publicPaths) {
			if (path.startsWith(publicPath)) {
				return true;
			}
		}

		return false;
	}
}