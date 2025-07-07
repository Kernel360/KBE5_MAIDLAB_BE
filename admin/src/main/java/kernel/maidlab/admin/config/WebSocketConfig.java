package kernel.maidlab.admin.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

import kernel.maidlab.admin.auth.websocket.CookieWebSocketAuthInterceptor;
import kernel.maidlab.admin.logs.LogWebSocketHandler;
import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSocket
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketConfigurer {

    private final LogWebSocketHandler logWebSocketHandler;
    private final CookieWebSocketAuthInterceptor cookieWebSocketAuthInterceptor;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(logWebSocketHandler, "/admin/logs/stream")
                .setAllowedOrigins("*")
                .addInterceptors(cookieWebSocketAuthInterceptor);
    }
}