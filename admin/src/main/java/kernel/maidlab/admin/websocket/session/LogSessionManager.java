package kernel.maidlab.admin.websocket.session;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

import kernel.maidlab.core.websocket.AbstractWebSocketSessionManager;
import kernel.maidlab.core.websocket.WebSocketMessage;
import kernel.maidlab.core.websocket.WebSocketUtils;

@Component
public class LogSessionManager extends AbstractWebSocketSessionManager {
	
	private final Set<WebSocketSession> sessions = Collections.synchronizedSet(new HashSet<>());
	private final ConcurrentHashMap<String, String> sessionAdminMap = new ConcurrentHashMap<>();
	
	@Override
	public void addSession(WebSocketSession session, String adminKey) {
		sessions.add(session);
		sessionAdminMap.put(session.getId(), adminKey);
	}
	
	@Override
	public void removeSession(WebSocketSession session) {
		sessions.remove(session);
		sessionAdminMap.remove(session.getId());
	}
	
	@Override
	public void broadcast(String message) {
		WebSocketMessage wsMessage = new WebSocketMessage("broadcast", message);
		WebSocketUtils.broadcastMessage(sessions, wsMessage);
		WebSocketUtils.removeClosedSessions(sessions, sessionAdminMap);
	}
	
	@Override
	public void sendToUser(String adminKey, String message) {
		WebSocketMessage wsMessage = new WebSocketMessage("direct", message);
		
		sessionAdminMap.entrySet().stream()
			.filter(entry -> adminKey.equals(entry.getValue()))
			.forEach(entry -> {
				sessions.stream()
					.filter(session -> session.getId().equals(entry.getKey()))
					.findFirst()
					.ifPresent(session -> {
						try {
							WebSocketUtils.sendMessage(session, wsMessage);
						} catch (Exception e) {
							// Log error but don't throw
						}
					});
			});
	}
	
	@Override
	public int getSessionCount() {
		return sessions.size();
	}
	
	public void broadcastLogUpdate(String newContent) {
		if (newContent != null && !newContent.trim().isEmpty()) {
			WebSocketMessage message = new WebSocketMessage("update", newContent);
			WebSocketUtils.broadcastMessage(sessions, message);
			WebSocketUtils.removeClosedSessions(sessions, sessionAdminMap);
		}
	}
	
	public boolean isEmpty() {
		return sessions.isEmpty();
	}
}
