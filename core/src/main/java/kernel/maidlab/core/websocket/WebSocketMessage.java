package kernel.maidlab.core.websocket;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class WebSocketMessage {
	
	private String type;
	private String content;
	private long timestamp;
	
	public WebSocketMessage() {
		this.timestamp = System.currentTimeMillis();
	}
	
	public WebSocketMessage(String type, String content) {
		this();
		this.type = type;
		this.content = content;
	}
	
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