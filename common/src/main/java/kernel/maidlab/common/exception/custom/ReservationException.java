package kernel.maidlab.common.exception.custom;

import kernel.maidlab.common.enums.ResponseType;
import kernel.maidlab.common.exception.BaseException;

public class ReservationException extends BaseException {
	public ReservationException(ResponseType responseType) {
		super(responseType);
	}
}

