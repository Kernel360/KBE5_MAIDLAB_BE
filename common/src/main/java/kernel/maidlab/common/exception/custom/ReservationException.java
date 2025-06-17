package kernel.maidlab.common.exception.custom;


import kernel.maidlab.common.exception.BaseException;
import kernel.maidlab.common.enums.ResponseType;

public class ReservationException extends BaseException
{
	public ReservationException(ResponseType responseType) {
		super(responseType);
	}
}

