package kernel.maidlab.common.dto;

import kernel.maidlab.common.enums.UserType;
import lombok.Getter;

public class JwtDto {

	@Getter
	public static class ValidationResult {
		private final boolean valid;
		private final String message;
		private final String uuid;
		private final UserType userType;

		public ValidationResult(boolean valid, String message, String uuid, UserType userType) {
			this.valid = valid;
			this.message = message;
			this.uuid = uuid;
			this.userType = userType;
		}

		public static ValidationResult success(String uuid, UserType userType) {
			return new ValidationResult(true, null, uuid, userType);
		}

		public static ValidationResult failure(String message) {
			return new ValidationResult(false, message, null, null);
		}

	}
}
