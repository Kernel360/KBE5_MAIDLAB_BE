package kernel.maidlab.admin.logs;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class LogWebSocketHandler extends TextWebSocketHandler {

	private final LogMonitoringService logMonitoringService;
	private final ObjectMapper objectMapper = new ObjectMapper();
	private final Set<WebSocketSession> sessions = Collections.synchronizedSet(new HashSet<>());
	private final ConcurrentHashMap<String, String> sessionAdminMap = new ConcurrentHashMap<>();
	private volatile boolean isMonitoring = false;

	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		// Authentication is already done by CookieWebSocketAuthInterceptor
		// Get admin key from session attributes (set by interceptor)
		String adminKey = (String)session.getAttributes().get("adminKey");

		sessions.add(session);
		sessionAdminMap.put(session.getId(), adminKey);

		// Send initial log content (last 50 lines)
		try {
			String initialContent = logMonitoringService.getTailLogContent(50);

			if (initialContent != null && !initialContent.trim().isEmpty()) {
				String[] lines = initialContent.split("\n");

				for (String line : lines) {
					if (!line.trim().isEmpty()) {
						sendMessage(session, new LogMessage("initial", line.trim()));
					}
				}

				sendMessage(session, new LogMessage("initial_complete", "Initial logs loaded"));
			} else {
				sendMessage(session, new LogMessage("initial", "No logs available"));
				sendMessage(session, new LogMessage("initial_complete", "No initial logs"));
			}
		} catch (Exception e) {
			log.error("Error sending initial log content to admin: {}", adminKey, e);
			sendMessage(session, new LogMessage("error", "Failed to load initial logs: " + e.getMessage()));
		}

		// Start monitoring if this is the first connection
		if (!isMonitoring) {
			startLogMonitoring();
		}
	}

	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
		sessions.remove(session);
		sessionAdminMap.remove(session.getId());

		// Stop monitoring if no more sessions
		if (sessions.isEmpty()) {
			stopLogMonitoring();
		}
	}

	@Override
	protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
		String payload = message.getPayload();

		try {
			// Parse JSON message or handle simple string commands
			String command;
			if (payload.startsWith("{")) {
				// JSON format
				JsonNode jsonNode = objectMapper.readTree(payload);
				command = jsonNode.get("type").asText();
			} else {
				// Simple string command (backward compatibility)
				command = payload;
			}

			// Handle commands (session is already authenticated by interceptor)
			switch (command) {
				case "get_full_log":
					String fullContent = logMonitoringService.getFullLogContent();
					sendMessage(session, new LogMessage("full", fullContent));
					break;
				case "get_tail_log":
					String tailContent = logMonitoringService.getTailLogContent(50);
					sendMessage(session, new LogMessage("tail", tailContent));
					break;
				default:
					sendMessage(session, new LogMessage("error", "Unknown command: " + command));
			}

		} catch (Exception e) {
			log.error("Error processing message", e);
			sendMessage(session, new LogMessage("error", "Invalid message format"));
		}
	}

	@Override
	public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
		log.error("WebSocket transport error for session {}: {}", session.getId(), exception.getMessage());
		sessions.remove(session);
		sessionAdminMap.remove(session.getId());
	}

	private void startLogMonitoring() {
		isMonitoring = true;
		logMonitoringService.startMonitoring(this::broadcastLogUpdate);
	}

	private void stopLogMonitoring() {
		isMonitoring = false;
		logMonitoringService.stopMonitoring();
	}

	private void broadcastLogUpdate(String newContent) {
		if (newContent != null && !newContent.trim().isEmpty()) {
			LogMessage message = new LogMessage("update", newContent);

			synchronized (sessions) {
				sessions.removeIf(session -> {
					try {
						sendMessage(session, message);
						return false;
					} catch (Exception e) {
						sessionAdminMap.remove(session.getId());
						return true; // Remove session if sending fails
					}
				});
			}
		}
	}

	private void sendMessage(WebSocketSession session, LogMessage message) throws IOException {
		if (session.isOpen()) {
			String json = objectMapper.writeValueAsString(message);
			session.sendMessage(new TextMessage(json));
		}
	}

	public static class LogMessage {
		public String type;
		public String content;
		public long timestamp;

		public LogMessage() {
		}

		public LogMessage(String type, String content) {
			this.type = type;
			this.content = content;
			this.timestamp = System.currentTimeMillis();
		}

		// Getters and setters for JSON serialization
		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		public String getContent() {
			return content;
		}

		public void setContent(String content) {
			this.content = content;
		}

		public long getTimestamp() {
			return timestamp;
		}

		public void setTimestamp(long timestamp) {
			this.timestamp = timestamp;
		}
	}
}
