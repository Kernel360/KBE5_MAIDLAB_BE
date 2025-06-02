package kernel.maidlab.api.auth.jwt;

import kernel.maidlab.common.enums.UserType;
import lombok.Getter;

public class JwtDto {

	public static class ValidationResult {
		private final boolean valid;
		@Getter
		private final String message;
		@Getter
		private final String uuid;
		@Getter
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

		public boolean isValid() {
			return valid;
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

	public static class TempTokenInfo {
		private final boolean valid;
		@Getter
		private final String message;
		@Getter
		private final String googleId;
		@Getter
		private final String googleName;
		@Getter
		private final UserType userType;

		private TempTokenInfo(boolean valid, String message, String googleId, String googleName, UserType userType) {
			this.valid = valid;
			this.message = message;
			this.googleId = googleId;
			this.googleName = googleName;
			this.userType = userType;
		}

		public static TempTokenInfo success(String googleId, String googleName, UserType userType) {
			return new TempTokenInfo(true, null, googleId, googleName, userType);
		}

		public static TempTokenInfo failure(String message) {
			return new TempTokenInfo(false, message, null, null, null);
		}

		public boolean isValid() {
			return valid;
		}
	}
}
