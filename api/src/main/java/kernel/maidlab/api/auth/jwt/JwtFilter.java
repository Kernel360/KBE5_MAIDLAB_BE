package kernel.maidlab.api.auth.jwt;

import kernel.maidlab.api.util.ServletResponseUtil;
import kernel.maidlab.common.enums.UserType;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class JwtFilter implements Filter {

	private static final Logger log = LoggerFactory.getLogger(JwtFilter.class);

	// request에 저장할 키 이름들
	public static final String CURRENT_USER_KEY = "currentUser";
	public static final String CURRENT_USER_UUID_KEY = "currentUserUuid";
	public static final String CURRENT_USER_TYPE_KEY = "currentUserType";

	private final JwtProvider jwtProvider;
	private final ObjectMapper objectMapper;

	public JwtFilter(JwtProvider jwtProvider, ObjectMapper objectMapper) {
		this.jwtProvider = jwtProvider;
		this.objectMapper = objectMapper;
	}

	private static final Set<String> AUTH_WHITELIST = Set.of("/api/auth/login", "/api/auth/social-login",
		"/api/auth/sign-up", "/api/auth/social-sign-up", "/api/auth/refresh");

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws
		IOException,
		ServletException {

		HttpServletRequest httpRequest = (HttpServletRequest)request;
		HttpServletResponse httpResponse = (HttpServletResponse)response;
		String uri = httpRequest.getRequestURI();
		String method = httpRequest.getMethod();

		// OPTIONS 요청은 필터를 통과시킴
		if (method.equals("OPTIONS")) {
			chain.doFilter(request, response);
			return;
		}

		// admin 경로는 이 필터에서 처리하지 않음
		if (uri.startsWith("/api/admin/")) {
			chain.doFilter(request, response);
			return;
		}

		if (AUTH_WHITELIST.contains(uri)) {
			chain.doFilter(request, response); // 필터 우회
			return;
		}

		log.debug("JWT 인증 필터 시작 - URL: {}", httpRequest.getRequestURI());

		boolean authenticated = false;

		try {
			String token = jwtProvider.extractToken(httpRequest);

			if (token != null) {
				log.debug("토큰 발견, 검증 시작");

				JwtDto.ValidationResult validationResult = jwtProvider.validateAccessToken(token);

				if (validationResult.isValid()) {
					String uuid = validationResult.getUuid();
					UserType userType = validationResult.getUserType();
					Object user = jwtProvider.findUser(uuid, userType);

					if (user != null) {
						httpRequest.setAttribute(CURRENT_USER_KEY, user);
						httpRequest.setAttribute(CURRENT_USER_UUID_KEY, uuid);
						httpRequest.setAttribute(CURRENT_USER_TYPE_KEY, userType);
						authenticated = true;

						log.debug("인증 성공 - UUID: {}, Type: {}", uuid, userType);
					} else {
						log.debug("사용자 조회 실패 - UUID: {}", uuid);
					}
				} else {
					log.debug("토큰 검증 실패: {}", validationResult.getMessage());
				}
			} else {
				log.debug("토큰이 없음");
			}

		} catch (Exception e) {
			log.error("JWT 인증 필터에서 오류 발생", e);
		}

		if (!authenticated) {
			ServletResponseUtil.authorizationFailed(httpResponse, objectMapper);
			return;
		}

		chain.doFilter(request, response);
	}

}
