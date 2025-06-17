package kernel.maidlab.common.exception.custom;

import kernel.maidlab.common.exception.BaseException;
import kernel.maidlab.common.enums.ResponseType;

public class AuthException extends BaseException {
	public AuthException(ResponseType responseType) {
		super(responseType);
	}
}
