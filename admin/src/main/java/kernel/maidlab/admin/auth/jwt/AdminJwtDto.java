package kernel.maidlab.admin.auth.jwt;

import lombok.Getter;

public class AdminJwtDto {

	@Getter
	public static class TokenPair {
		private final String accessToken;
		private final String refreshToken;

		public TokenPair(String accessToken, String refreshToken) {
			this.accessToken = accessToken;
			this.refreshToken = refreshToken;
		}
	}

	public static class AdminValidationResult {
		private final boolean valid;
		@Getter
		private final String message;
		@Getter
		private final String adminKey;

		private AdminValidationResult(boolean valid, String message, String adminKey) {
			this.valid = valid;
			this.message = message;
			this.adminKey = adminKey;
		}

		public static AdminValidationResult success(String adminKey) {
			return new AdminValidationResult(true, null, adminKey);
		}

		public static AdminValidationResult failure(String message) {
			return new AdminValidationResult(false, message, null);
		}

		public boolean isValid() {
			return valid;
		}
	}

	@Getter
	public static class AdminRefreshResult {
		private final boolean success;
		private final String message;
		private final String accessToken;
		private final String refreshToken;

		private AdminRefreshResult(boolean success, String message, String accessToken, String refreshToken) {
			this.success = success;
			this.message = message;
			this.accessToken = accessToken;
			this.refreshToken = refreshToken;
		}

		public static AdminRefreshResult success(String accessToken, String refreshToken) {
			return new AdminRefreshResult(true, null, accessToken, refreshToken);
		}

		public static AdminRefreshResult failure(String message) {
			return new AdminRefreshResult(false, message, null, null);
		}
	}
}