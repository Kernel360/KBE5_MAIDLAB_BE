package kernel.maidlab.admin.websocket;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

import kernel.maidlab.admin.websocket.interceptor.CookieWebSocketAuthInterceptor;
import kernel.maidlab.admin.websocket.handler.LogWebSocketHandler;
import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSocket
@RequiredArgsConstructor
public class AdminWebSocketConfig implements WebSocketConfigurer {

	private final LogWebSocketHandler logWebSocketHandler;
	private final CookieWebSocketAuthInterceptor cookieWebSocketAuthInterceptor;

	@Override
	public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
		registry.addHandler(logWebSocketHandler, "/admin/logs/stream")
			.setAllowedOrigins("https://www.maidlab.site", "http://localhost:5173", "http://localhost:8080",
				"http://127.0.0.1:3000")
			.setAllowedOriginPatterns("https://www.maidlab.site", "http://localhost:*", "http://127.0.0.1:*")
			.addInterceptors(cookieWebSocketAuthInterceptor);
	}
}
