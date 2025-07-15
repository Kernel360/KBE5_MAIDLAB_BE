package kernel.maidlab.core.exception;

import java.time.LocalDateTime;

import kernel.maidlab.common.enums.ResponseType;
import lombok.Getter;

@Getter
public class BaseException extends RuntimeException {
	private final ResponseType responseType;
	private final LocalDateTime timestamp;
	private final String customMessage;

	public BaseException(ResponseType responseType) {
		super(responseType.getMessage());
		this.responseType = responseType;
		this.timestamp = LocalDateTime.now();
		this.customMessage = null;
	}

	public BaseException(ResponseType responseType, String customMessage) {
		super(customMessage != null ? customMessage : responseType.getMessage());
		this.responseType = responseType;
		this.timestamp = LocalDateTime.now();
		this.customMessage = customMessage;
	}

	public BaseException(ResponseType responseType, Throwable cause) {
		super(responseType.getMessage(), cause);
		this.responseType = responseType;
		this.timestamp = LocalDateTime.now();
		this.customMessage = null;
	}

	public BaseException(ResponseType responseType, String customMessage, Throwable cause) {
		super(customMessage != null ? customMessage : responseType.getMessage(), cause);
		this.responseType = responseType;
		this.timestamp = LocalDateTime.now();
		this.customMessage = customMessage;
	}

	public String getDisplayMessage() {
		return customMessage != null ? customMessage : responseType.getMessage();
	}
}