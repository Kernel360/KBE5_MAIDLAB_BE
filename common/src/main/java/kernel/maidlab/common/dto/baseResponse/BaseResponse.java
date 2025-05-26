package kernel.maidlab.common.dto.baseResponse;

import kernel.maidlab.common.enums.ResponseCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class BaseResponse<T> {
	private String code;
	private String message;
	private T data;

	public static <T> BaseResponse<T> success(T data) {
		return new BaseResponse<>(ResponseCode.SU.name(), ResponseCode.SU.getMessage(), data);
	}

	public static <T> BaseResponse<T> error(ResponseCode code) {
		return new BaseResponse<>(code.name(), code.getMessage(), null);
	}
}