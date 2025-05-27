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

	@Getter
	public static class TokenPair {
		private final String accessToken;
		private final String refreshToken;

		public TokenPair(String accessToken, String refreshToken) {
			this.accessToken = accessToken;
			this.refreshToken = refreshToken;
		}
	}

	@Getter
	public static class RefreshResult {
		private final boolean success;
		private final String message;
		private final String accessToken;
		private final String refreshToken;

		private RefreshResult(boolean success, String message, String accessToken, String refreshToken) {
			this.success = success;
			this.message = message;
			this.accessToken = accessToken;
			this.refreshToken = refreshToken;
		}

		public static RefreshResult success(String accessToken, String refreshToken) {
			return new RefreshResult(true, null, accessToken, refreshToken);
		}

		public static RefreshResult failure(String message) {
			return new RefreshResult(false, message, null, null);
		}
	}
}
