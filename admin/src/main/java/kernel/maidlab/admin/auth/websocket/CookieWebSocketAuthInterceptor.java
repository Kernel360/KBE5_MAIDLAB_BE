package kernel.maidlab.admin.auth.websocket;

import kernel.maidlab.admin.auth.jwt.AdminJwtProvider;
import kernel.maidlab.common.dto.auth.AdminJwtDto;
import kernel.maidlab.common.util.CookieUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Map;

@Component
public class CookieWebSocketAuthInterceptor implements HandshakeInterceptor {

    private static final Logger log = LoggerFactory.getLogger(CookieWebSocketAuthInterceptor.class);
    
    private final AdminJwtProvider adminJwtProvider;
    private final CookieUtil cookieUtil;
    
    public CookieWebSocketAuthInterceptor(AdminJwtProvider adminJwtProvider, CookieUtil cookieUtil) {
        this.adminJwtProvider = adminJwtProvider;
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
            
            HttpServletRequest servletRequest = ((ServletServerHttpRequest) request).getServletRequest();
            
            // Extract refresh token from cookie
            String cookieRefreshToken = cookieUtil.getRefreshTokenFromCookie(servletRequest);
            
            if (cookieRefreshToken == null) {
                log.warn("WebSocket handshake failed: No refresh token cookie found");
                return false;
            }
            
            // Validate refresh token (includes DB token matching)
            AdminJwtDto.AdminValidationResult validationResult = 
                adminJwtProvider.validateAdminToken(cookieRefreshToken, "refresh");
            
            if (!validationResult.isValid()) {
                log.warn("WebSocket handshake failed: {}", validationResult.getMessage());
                return false;
            }
            
            // Authentication successful - store admin key in session attributes
            String adminKey = validationResult.getAdminKey();
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