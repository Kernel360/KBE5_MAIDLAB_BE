package kernel.maidlab.common.jwt;

import kernel.maidlab.common.dto.JwtDto;
import kernel.maidlab.common.enums.UserType;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Set;

@Component
public class JwtFilter implements Filter {

	private static final Logger log = LoggerFactory.getLogger(JwtFilter.class);

	// request에 저장할 키 이름들
	public static final String CURRENT_USER_KEY = "currentUser";
	public static final String CURRENT_USER_UUID_KEY = "currentUserUuid";
	public static final String CURRENT_USER_TYPE_KEY = "currentUserType";

	private final JwtProvider jwtProvider;

	public JwtFilter(JwtProvider jwtProvider) {
		this.jwtProvider = jwtProvider;
	}

	private static final Set<String> AUTH_WHITELIST = Set.of(
		"/api/auth/login",
		"/api/auth/register"
	);

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
		throws IOException, ServletException {

		HttpServletRequest httpRequest = (HttpServletRequest)request;
		String uri = httpRequest.getRequestURI();

		if (AUTH_WHITELIST.contains(uri)) {
			chain.doFilter(request, response); // 필터 우회
			return;
		}

		log.debug("JWT 인증 필터 시작 - URL: {}", httpRequest.getRequestURI());

		try {
			String token = jwtProvider.extractToken(httpRequest);

			if (token != null) {
				log.debug("토큰 발견, 검증 시작");

				JwtDto.ValidationResult validationResult = jwtProvider.validateToken(token);

				if (validationResult.isValid()) {
					String uuid = validationResult.getUuid();
					UserType userType = validationResult.getUserType();
					Object user = jwtProvider.findUser(uuid, userType);

					if (user != null) {
						httpRequest.setAttribute(CURRENT_USER_KEY, user);
						httpRequest.setAttribute(CURRENT_USER_UUID_KEY, uuid);
						httpRequest.setAttribute(CURRENT_USER_TYPE_KEY, userType);

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

		chain.doFilter(request, response);
	}

}
