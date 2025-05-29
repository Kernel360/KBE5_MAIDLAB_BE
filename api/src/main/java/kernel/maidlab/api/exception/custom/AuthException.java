package kernel.maidlab.api.exception.custom;

import kernel.maidlab.api.exception.BaseException;
import kernel.maidlab.common.enums.ResponseType;

public class AuthException extends BaseException {
	public AuthException(ResponseType responseType) {
		super(responseType);
	}
}
