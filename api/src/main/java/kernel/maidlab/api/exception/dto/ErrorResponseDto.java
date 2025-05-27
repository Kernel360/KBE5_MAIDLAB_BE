package kernel.maidlab.api.exception.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

import kernel.maidlab.common.enums.ResponseType;
import lombok.Getter;

@Getter
public class ErrorResponseDto {
	private final String code;
	private final String message;
	private final LocalDateTime timestamp;

	public ErrorResponseDto(ResponseType type) {
		this.code = type.getCode();
		this.message = type.getMessage();
		this.timestamp = LocalDateTime.now();
	}
}
