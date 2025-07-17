package kernel.maidlab.common.enums;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public enum ResponseType {

	// 200
	SUCCESS("SU", "Success.", HttpStatus.OK),

	// 400 Bad Request - 잘못된 요청
	VALIDATION_FAILED("VF", "Validation failed.", HttpStatus.BAD_REQUEST),
	DUPLICATE_TEL_NUMBER("DT", "Duplicate tel number.", HttpStatus.BAD_REQUEST),
	DUPLICATE_RESERVATION_ID("DR", "Matching notification has already been sent.", HttpStatus.BAD_REQUEST),
	WRONG_ADDRESS("WR", "You sent the wrong address.", HttpStatus.BAD_REQUEST),
	ALREADY_CHECKED_IN("ACI", "Already checked in.", HttpStatus.BAD_REQUEST),
	ALREADY_CHECKED_OUT("ACO", "Already checked out.", HttpStatus.BAD_REQUEST),
	ALREADY_WORKING_OR_COMPLETED("AWC", "Already working or completed.", HttpStatus.BAD_REQUEST),
	INVALID_USER_TYPE("IUT", "Invalid user_type or None user_type.", HttpStatus.BAD_REQUEST),
	INSUFFICIENT_POINT("IP", "사용 가능한 포인트가 부족합니다.", HttpStatus.BAD_REQUEST),
	ALREADY_REVIEWED("AR", "Review already exists for this reservation.", HttpStatus.BAD_REQUEST),

	// 401 Unauthorized - 인증 실패
	AUTHORIZATION_FAILED("AF", "Authorization Failed.", HttpStatus.UNAUTHORIZED),
	LOGIN_FAILED("LF", "Login Failed.", HttpStatus.UNAUTHORIZED),
	INVALID_REFRESH_TOKEN("IRT", "Invalid refresh token.", HttpStatus.UNAUTHORIZED),
	ACCOUNT_DELETED("AD", "Account has been deleted.", HttpStatus.UNAUTHORIZED),

	// 402 Payment Required - 결제 실패
	PAYMENT_FAILED("PF", "Payment failed.", HttpStatus.PAYMENT_REQUIRED),

	// 403 Forbidden - 권한 없음
	DO_NOT_HAVE_PERMISSION("NP", "Do not have permission.", HttpStatus.FORBIDDEN),

	// 404 Not Found - 리소스 찾을 수 없음
	THIS_RESOURCE_DOES_NOT_EXIST("TRDE", "This resource does not exist.", HttpStatus.NOT_FOUND),
	THIS_BOARD_DOES_NOT_EXIST("TBDE", "This board does not exist.", HttpStatus.NOT_FOUND),
	THIS_USER_DOES_NOT_EXIST("TUDE", "This user does not exist.", HttpStatus.NOT_FOUND),
	THIS_RESERVATION_DOSE_NOT_EXIST("TRDE2", "This reservation does not exist.", HttpStatus.NOT_FOUND),
	AVAILABLE_MANAGER_DOES_NOT_EXIST("AMDE", "Available manager does not exist.", HttpStatus.NOT_FOUND),

	// 500 Internal Server Error
	INTERNAL_SERVER_ERROR("ISE", "Internal server error.", HttpStatus.INTERNAL_SERVER_ERROR),
	DATABASE_ERROR("DBE", "Database error.", HttpStatus.INTERNAL_SERVER_ERROR),

	// 502 Bad Gateway - 외부 서비스 오류
	EXTERNAL_SERVICE_ERROR("ESE", "External service error.", HttpStatus.BAD_GATEWAY),

	// 503 Service Unavailable - 서비스 일시적 사용 불가
	SERVICE_UNAVAILABLE("SU", "Service temporarily unavailable.", HttpStatus.SERVICE_UNAVAILABLE);

	private final String code;
	private final String message;
	private final HttpStatus httpStatus;

	ResponseType(String code, String message, HttpStatus httpStatus) {
		this.code = code;
		this.message = message;
		this.httpStatus = httpStatus;
	}
}
