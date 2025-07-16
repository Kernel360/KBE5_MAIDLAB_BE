package kernel.maidlab.core.websocket;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

@Component
public class WebSocketSessionManager {
	
	private final Set<WebSocketSession> sessions = Collections.synchronizedSet(new HashSet<>());
	private final ConcurrentHashMap<String, String> sessionUserMap = new ConcurrentHashMap<>();
	
	public void addSession(WebSocketSession session, String userKey) {
		sessions.add(session);
		sessionUserMap.put(session.getId(), userKey);
	}
	
	public void removeSession(WebSocketSession session) {
		sessions.remove(session);
		sessionUserMap.remove(session.getId());
	}
	
	public void broadcast(WebSocketMessage message) {
		WebSocketUtils.broadcastMessage(sessions, message);
		removeClosedSessions();
	}
	
	public void broadcast(String type, String content) {
		broadcast(new WebSocketMessage(type, content));
	}
	
	
	public boolean isEmpty() {
		return sessions.isEmpty();
	}
	
	private void removeClosedSessions() {
		WebSocketUtils.removeClosedSessions(sessions, sessionUserMap);
	}
}