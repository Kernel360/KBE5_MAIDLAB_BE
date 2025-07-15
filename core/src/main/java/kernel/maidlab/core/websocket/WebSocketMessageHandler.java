package kernel.maidlab.core.websocket;

import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

public abstract class WebSocketMessageHandler extends TextWebSocketHandler {
	
	public abstract void afterConnectionEstablished(WebSocketSession session) throws Exception;
	
	public abstract void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception;
	
	public abstract void afterConnectionClosed(WebSocketSession session, 
		org.springframework.web.socket.CloseStatus status) throws Exception;
	
	public abstract void handleTransportError(WebSocketSession session, Throwable exception) throws Exception;
}