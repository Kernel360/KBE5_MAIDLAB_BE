package kernel.maidlab.core.websocket;

import org.springframework.web.socket.WebSocketSession;

public abstract class AbstractWebSocketSessionManager {
	
	public abstract void addSession(WebSocketSession session, String userKey);
	
	public abstract void removeSession(WebSocketSession session);
	
	public abstract void broadcast(String message);
	
	public abstract void sendToUser(String userKey, String message);
	
	public abstract int getSessionCount();
}