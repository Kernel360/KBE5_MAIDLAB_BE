package kernel.maidlab.api.exception.dto;

import kernel.maidlab.common.enums.ResponseType;
import lombok.Getter;

@Getter
public class ErrorResponseDto {
	private final String code;
	private final String message;
	private final int status;

	public ErrorResponseDto(ResponseType type) {
		this.code = type.getCode();
		this.message = type.getMessage();
		this.status = type.getHttpStatus().value();
	}
}
