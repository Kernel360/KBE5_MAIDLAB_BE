package kernel.maidlab.admin.logs;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

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
    private volatile boolean isMonitoring = false;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        sessions.add(session);
        
        // Send initial log content (last 5 lines) - send each line separately
        String initialContent = logMonitoringService.getTailLogContent(5);
        String[] lines = initialContent.split("\n");
        for (String line : lines) {
            if (!line.trim().isEmpty()) {
                sendMessage(session, new LogMessage("initial", line));
            }
        }
        
        // Start monitoring if this is the first connection
        if (!isMonitoring) {
            startLogMonitoring();
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        sessions.remove(session);
        
        // Stop monitoring if no more sessions
        if (sessions.isEmpty()) {
            stopLogMonitoring();
        }
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();

        // Handle different message types
        switch (payload) {
            case "get_full_log":
                String fullContent = logMonitoringService.getFullLogContent();
                sendMessage(session, new LogMessage("full", fullContent));
                break;
            case "get_tail_log":
                String tailContent = logMonitoringService.getTailLogContent(50);
                sendMessage(session, new LogMessage("tail", tailContent));
                break;
            default:
                sendMessage(session, new LogMessage("error", "Unknown command: " + payload));
        }
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        log.error("WebSocket transport error for session {}: {}", session.getId(), exception.getMessage());
        sessions.remove(session);
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

        public LogMessage() {}

        public LogMessage(String type, String content) {
            this.type = type;
            this.content = content;
            this.timestamp = System.currentTimeMillis();
        }

        // Getters and setters for JSON serialization
        public String getType() { return type; }
        public void setType(String type) { this.type = type; }
        
        public String getContent() { return content; }
        public void setContent(String content) { this.content = content; }
        
        public long getTimestamp() { return timestamp; }
        public void setTimestamp(long timestamp) { this.timestamp = timestamp; }
    }
}
