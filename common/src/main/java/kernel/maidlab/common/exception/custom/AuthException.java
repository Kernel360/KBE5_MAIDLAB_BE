package kernel.maidlab.common.exception.custom;

import kernel.maidlab.common.enums.ResponseType;
import kernel.maidlab.common.exception.BaseException;

public class AuthException extends BaseException {
	public AuthException(ResponseType responseType) {
		super(responseType);
	}
}
