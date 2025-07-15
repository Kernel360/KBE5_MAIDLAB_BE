package kernel.maidlab.admin.auth.websocket;

import jakarta.servlet.http.HttpServletRequest;
import kernel.maidlab.admin.auth.service.AdminTokenService;
import kernel.maidlab.common.util.CookieUtil;
import kernel.maidlab.core.security.jwt.AdminJwtProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

@Component
public class CookieWebSocketAuthInterceptor implements HandshakeInterceptor {

	private static final Logger log = LoggerFactory.getLogger(CookieWebSocketAuthInterceptor.class);

	private final AdminJwtProvider adminJwtProvider;
	private final AdminTokenService adminTokenService;
	private final CookieUtil cookieUtil;

	public CookieWebSocketAuthInterceptor(AdminJwtProvider adminJwtProvider, AdminTokenService adminTokenService,
		CookieUtil cookieUtil) {
		this.adminJwtProvider = adminJwtProvider;
		this.adminTokenService = adminTokenService;
		this.cookieUtil = cookieUtil;
	}

	@Override
	public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response,
		WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {

		try {
			// Extract HttpServletRequest from ServerHttpRequest
			if (!(request instanceof ServletServerHttpRequest)) {
				log.warn("WebSocket handshake failed: Not a servlet request");
				return false;
			}

			HttpServletRequest servletRequest = ((ServletServerHttpRequest)request).getServletRequest();

			// Extract refresh token from cookie
			String cookieRefreshToken = cookieUtil.getRefreshTokenFromCookie(servletRequest);

			if (cookieRefreshToken == null) {
				log.warn("WebSocket handshake failed: No refresh token cookie found");
				return false;
			}

			// Validate refresh token and check DB
			if (!adminJwtProvider.validateAdminRefreshToken(cookieRefreshToken)) {
				log.warn("WebSocket handshake failed: Invalid refresh token");
				return false;
			}

			String adminKey = adminJwtProvider.getAdminKey(cookieRefreshToken);
			
			// Check if token matches stored token in DB
			String storedToken = adminTokenService.getStoredAdminRefreshToken(adminKey);
			if (storedToken == null || !storedToken.equals(cookieRefreshToken)) {
				log.warn("WebSocket handshake failed: Refresh token does not match stored token");
				return false;
			}

			// Authentication successful - store admin key in session attributes
			attributes.put("adminKey", adminKey);

			return true;

		} catch (Exception e) {
			log.error("WebSocket handshake error", e);
			return false;
		}
	}

	@Override
	public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response,
		WebSocketHandler wsHandler, Exception exception) {
		if (exception != null) {
			log.error("WebSocket handshake completed with error", exception);
		}
	}
}