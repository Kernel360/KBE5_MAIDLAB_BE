package kernel.maidlab.admin.auth.websocket;

import kernel.maidlab.core.security.jwt.AdminJwtProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.net.URI;
import java.util.Map;

@Component
public class WebSocketAuthInterceptor implements HandshakeInterceptor {

	private static final Logger log = LoggerFactory.getLogger(WebSocketAuthInterceptor.class);

	private final AdminJwtProvider adminJwtProvider;

	public WebSocketAuthInterceptor(AdminJwtProvider adminJwtProvider) {
		this.adminJwtProvider = adminJwtProvider;
	}

	@Override
	public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response,
		WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {

		try {
			// Extract token from query parameters
			String token = extractTokenFromQuery(request.getURI());

			if (token == null) {
				log.warn("WebSocket handshake failed: No token provided");
				return false;
			}

			// Validate the token
			if (!adminJwtProvider.validateAdminAccessToken(token)) {
				log.warn("WebSocket handshake failed: Invalid token");
				return false;
			}

			// Store admin key in session attributes for later use
			String adminKey = adminJwtProvider.getAdminKey(token);
			attributes.put("adminKey", adminKey);

			log.info("WebSocket handshake successful for admin: {}", adminKey);
			return true;

		} catch (Exception e) {
			log.error("WebSocket handshake error", e);
			return false;
		}
	}

	@Override
	public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response,
		WebSocketHandler wsHandler, Exception exception) {
		// Nothing to do after handshake
	}

	private String extractTokenFromQuery(URI uri) {
		if (uri == null || uri.getQuery() == null) {
			return null;
		}

		String query = uri.getQuery();
		String[] params = query.split("&");

		for (String param : params) {
			String[] keyValue = param.split("=");
			if (keyValue.length == 2 && "token".equals(keyValue[0])) {
				return keyValue[1];
			}
		}

		return null;
	}
}