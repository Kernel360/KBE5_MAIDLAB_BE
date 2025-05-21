package kernel.maidlab.common.dto;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ResponseType {

    // 200
    SUCCESS("SU", "Success.", HttpStatus.OK),

    // 400
    VALIDATION_FAILED("VF", "Validation failed.", HttpStatus.BAD_REQUEST),
    DUPLICATE_EMAIL("DE", "Duplicate email.", HttpStatus.BAD_REQUEST),
    DUPLICATE_TEL_NUMBER("DT", "Duplicate tel number.", HttpStatus.BAD_REQUEST),
    DUPLICATE_NICKNAME("DN", "Duplicate nickname.", HttpStatus.BAD_REQUEST),
    NOT_EXISTED_USER("NU", "This user does not exist.", HttpStatus.BAD_REQUEST),
    NOT_EXISTED_BOARD("NB", "This board does not exist.", HttpStatus.BAD_REQUEST),

    // 401
    SIGN_IN_FAIL("SF", "Login information mismatch.", HttpStatus.UNAUTHORIZED),
    AUTHORIZATION_FAILED("AF", "Authorization failed.", HttpStatus.UNAUTHORIZED),

    // 403
    NO_PERMISSION("NP", "Do not have permission.", HttpStatus.FORBIDDEN),

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
