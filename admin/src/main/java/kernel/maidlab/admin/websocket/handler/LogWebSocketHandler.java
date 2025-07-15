package kernel.maidlab.admin.websocket.handler;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import kernel.maidlab.admin.logs.LogMonitoringService;
import kernel.maidlab.core.websocket.WebSocketMessage;
import kernel.maidlab.core.websocket.WebSocketMessageHandler;
import kernel.maidlab.core.websocket.WebSocketSessionManager;
import kernel.maidlab.core.websocket.WebSocketUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class LogWebSocketHandler extends WebSocketMessageHandler {

	private final LogMonitoringService logMonitoringService;
	private final WebSocketSessionManager sessionManager;
	private final ObjectMapper objectMapper = new ObjectMapper();
	private volatile boolean isMonitoring = false;

	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		// Authentication is already done by CookieWebSocketAuthInterceptor
		// Get admin key from session attributes (set by interceptor)
		String adminKey = (String)session.getAttributes().get("adminKey");

		sessionManager.addSession(session, adminKey);

		// Send initial log content (last 50 lines)
		try {
			String initialContent = logMonitoringService.getTailLogContent(50);

			if (initialContent != null && !initialContent.trim().isEmpty()) {
				String[] lines = initialContent.split("\n");

				for (String line : lines) {
					if (!line.trim().isEmpty()) {
						WebSocketUtils.sendMessage(session, new WebSocketMessage("initial", line.trim()));
					}
				}

				WebSocketUtils.sendMessage(session, new WebSocketMessage("initial_complete", "Initial logs loaded"));
			} else {
				WebSocketUtils.sendMessage(session, new WebSocketMessage("initial", "No logs available"));
				WebSocketUtils.sendMessage(session, new WebSocketMessage("initial_complete", "No initial logs"));
			}
		} catch (Exception e) {
			log.error("Error sending initial log content to admin: {}", adminKey, e);
			WebSocketUtils.sendMessage(session, new WebSocketMessage("error", "Failed to load initial logs: " + e.getMessage()));
		}

		// Start monitoring if this is the first connection
		if (!isMonitoring) {
			startLogMonitoring();
		}
	}

	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
		sessionManager.removeSession(session);

		// Stop monitoring if no more sessions
		if (sessionManager.isEmpty()) {
			stopLogMonitoring();
		}
	}

	@Override
	public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
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
					WebSocketUtils.sendMessage(session, new WebSocketMessage("full", fullContent));
					break;
				case "get_tail_log":
					String tailContent = logMonitoringService.getTailLogContent(50);
					WebSocketUtils.sendMessage(session, new WebSocketMessage("tail", tailContent));
					break;
				default:
					WebSocketUtils.sendMessage(session, new WebSocketMessage("error", "Unknown command: " + command));
			}

		} catch (Exception e) {
			log.error("Error processing message", e);
			WebSocketUtils.sendMessage(session, new WebSocketMessage("error", "Invalid message format"));
		}
	}

	@Override
	public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
		log.error("WebSocket transport error for session {}: {}", session.getId(), exception.getMessage());
		sessionManager.removeSession(session);
	}

	private void startLogMonitoring() {
		isMonitoring = true;
		logMonitoringService.startMonitoring(newContent -> {
			if (newContent != null && !newContent.trim().isEmpty()) {
				sessionManager.broadcast("update", newContent);
			}
		});
	}

	private void stopLogMonitoring() {
		isMonitoring = false;
		logMonitoringService.stopMonitoring();
	}

}
