package kernel.maidlab.core.websocket;

import java.io.IOException;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import com.fasterxml.jackson.databind.ObjectMapper;

public class WebSocketUtils {
	
	private static final ObjectMapper objectMapper = new ObjectMapper();
	
	public static void sendMessage(WebSocketSession session, WebSocketMessage message) throws IOException {
		if (session.isOpen()) {
			String json = objectMapper.writeValueAsString(message);
			session.sendMessage(new TextMessage(json));
		}
	}
	
	public static void broadcastMessage(Set<WebSocketSession> sessions, WebSocketMessage message) {
		String json;
		try {
			json = objectMapper.writeValueAsString(message);
		} catch (Exception e) {
			return;
		}
		
		sessions.removeIf(session -> {
			try {
				if (session.isOpen()) {
					session.sendMessage(new TextMessage(json));
					return false;
				}
				return true;
			} catch (Exception e) {
				return true;
			}
		});
	}
	
	public static void removeClosedSessions(Set<WebSocketSession> sessions, 
			ConcurrentHashMap<String, String> sessionMap) {
		sessions.removeIf(session -> {
			if (!session.isOpen()) {
				sessionMap.remove(session.getId());
				return true;
			}
			return false;
		});
	}
}