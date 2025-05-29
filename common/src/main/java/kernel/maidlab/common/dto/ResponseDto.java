package kernel.maidlab.common.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import kernel.maidlab.common.enums.ResponseType;

import lombok.AllArgsConstructor;
import lombok.Getter;

import org.springframework.http.ResponseEntity;

@Getter
@AllArgsConstructor
public class ResponseDto<T> {

	@JsonIgnore
	private final ResponseType responseType;
	private final T data;

	@JsonProperty("code")
	public String getCode() {
		return responseType.getCode();
	}

	@JsonProperty("message")
	public String getMessage() {
		return responseType.getMessage();
	}

	public static <T> ResponseEntity<ResponseDto<T>> success(T data) {
		return ResponseEntity
			.status(ResponseType.SUCCESS.getHttpStatus())
			.body(new ResponseDto<T>(ResponseType.SUCCESS, data));
	}

	public static <T> ResponseEntity<ResponseDto<T>> success(ResponseType responseType, T data) {
		return ResponseEntity
			.status(responseType.getHttpStatus())
			.body(new ResponseDto<>(responseType, data));
	}

}

