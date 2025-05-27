package kernel.maidlab.common.enums;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ResponseType {

    // 200
    SUCCESS("SU", "Success.", HttpStatus.OK),

    // 400
    VALIDATION_FAILED("VF", "Validation failed.", HttpStatus.BAD_REQUEST),
    DUPLICATE_TEL_NUMBER("DT", "Duplicate tel number.", HttpStatus.BAD_REQUEST),

    // 401
    AUTHORIZATION_FAILED("AF", "Authorization Failed.", HttpStatus.UNAUTHORIZED),
    LOGIN_FAILED("LF", "Login Failed.", HttpStatus.UNAUTHORIZED),
    INVALID_REFRESH_TOKEN("RF", "Invalid refresh token.", HttpStatus.UNAUTHORIZED),

    // 403
    DO_NOT_HAVE_PERMISSION("NP", "Do not have permission.", HttpStatus.FORBIDDEN),

	//404
	THIS_RESOURCE_DOES_NOT_EXIST("NR", "This resource does not exist.", HttpStatus.NOT_FOUND),
	THIS_BOARD_DOES_NOT_EXIST("NB", "This board does not exist.", HttpStatus.NOT_FOUND),
	THIS_USER_DOES_NOT_EXIST("NU", "This user does not exist.", HttpStatus.NOT_FOUND),
	THIS_RESERVATION_DOSE_NOT_EXIST("NR", "This reservation does not exist.", HttpStatus.NOT_FOUND),

	// 500
	DATABASE_ERROR("DBE", "Database error.", HttpStatus.INTERNAL_SERVER_ERROR);

    private final String code;
    private final String message;
    private final HttpStatus httpStatus;

    ResponseType(String code, String message, HttpStatus httpStatus) {
        this.code = code;
        this.message = message;
        this.httpStatus = httpStatus;
    }
}
