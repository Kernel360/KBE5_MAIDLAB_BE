package kernel.maidlab.admin.auth.jwt;

import kernel.maidlab.admin.auth.entity.Admin;
import kernel.maidlab.api.util.ServletResponseUtil;

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
public class AdminJwtFilter implements Filter {

	private static final Logger log = LoggerFactory.getLogger(AdminJwtFilter.class);

	public static final String CURRENT_ADMIN_KEY = "currentAdmin";
	public static final String CURRENT_ADMIN_KEY_VALUE = "currentAdminKey";

	private final AdminJwtProvider adminJwtProvider;
	private final ObjectMapper objectMapper;

	public AdminJwtFilter(AdminJwtProvider adminJwtProvider, ObjectMapper objectMapper) {
		this.adminJwtProvider = adminJwtProvider;
		this.objectMapper = objectMapper;
	}

	// 관리자 인증이 필요 없는 URL들
	private static final Set<String> ADMIN_AUTH_WHITELIST = Set.of(
		"/api/admin/auth/login",
		"/api/admin/auth/refresh"
	);

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws
		IOException,
		ServletException {

		HttpServletRequest httpRequest = (HttpServletRequest)request;
		HttpServletResponse httpResponse = (HttpServletResponse)response;
		String uri = httpRequest.getRequestURI();

		// 관리자 모듈이 아닌 경우 필터 우회
		if (!uri.startsWith("/api/admin/")) {
			chain.doFilter(request, response);
			return;
		}

		// 인증이 필요 없는 URL인 경우 필터 우회
		if (ADMIN_AUTH_WHITELIST.contains(uri)) {
			chain.doFilter(request, response);
			return;
		}

		log.debug("관리자 JWT 인증 필터 시작 - URL: {}", uri);

		boolean authenticated = false;

		try {
			String token = adminJwtProvider.extractToken(httpRequest);

			if (token != null) {
				log.debug("관리자 토큰 발견, 검증 시작");

				AdminJwtDto.AdminValidationResult validationResult = adminJwtProvider.validateAdminAccessToken(token);

				if (validationResult.isValid()) {
					String adminKey = validationResult.getAdminKey();
					Admin admin = adminJwtProvider.findAdmin(adminKey);

					if (admin != null && !admin.getIsDeleted()) {
						httpRequest.setAttribute(CURRENT_ADMIN_KEY, admin);
						httpRequest.setAttribute(CURRENT_ADMIN_KEY_VALUE, adminKey);
						authenticated = true;
					}
				}
			}

		} catch (Exception e) {
			log.error("관리자 JWT 인증 필터에서 오류 발생", e);
		}

		if (!authenticated) {
			ServletResponseUtil.authorizationFailed(httpResponse, objectMapper);
			return;
		}

		chain.doFilter(request, response);
	}
}
