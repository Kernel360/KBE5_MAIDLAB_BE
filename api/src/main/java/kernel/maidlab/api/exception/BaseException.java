package kernel.maidlab.api.exception;

import kernel.maidlab.common.enums.ResponseType;
import lombok.Getter;

@Getter
public abstract class BaseException extends RuntimeException {
	private final ResponseType responseType;

	public BaseException(ResponseType responseType) {
		super(responseType.getMessage());
		this.responseType = responseType;
	}

}
