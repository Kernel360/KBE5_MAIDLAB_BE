package kernel.maidlab.api.exception.custom;


import kernel.maidlab.api.exception.BaseException;
import kernel.maidlab.common.enums.ResponseType;

public class ReservationException extends BaseException
{
	public ReservationException(ResponseType responseType) {
		super(responseType);
	}
}

