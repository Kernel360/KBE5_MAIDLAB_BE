package kernel.maidlab.common.exception.custom;

import kernel.maidlab.common.enums.ResponseType;
import kernel.maidlab.common.exception.BaseException;

public class PointException extends BaseException {

    public PointException(ResponseType responseType) {
        super(responseType);
    }
}
