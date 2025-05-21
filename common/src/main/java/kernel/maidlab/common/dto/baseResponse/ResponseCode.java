package kernel.maidlab.common.dto.baseResponse;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ResponseCode {
	SU("Success"),
	SF("Login information mismatch."),
	DBE("Database error."),
	VF("Validation failed."),
	DN("Duplicated nickname."),
	AF("Authorization Failed."),
	RF("Invalid refresh token."),
	AL("Already logged out.");

	private final String message;
}
