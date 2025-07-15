package kernel.maidlab.core.exception.custom;

import kernel.maidlab.common.enums.ResponseType;
import kernel.maidlab.core.exception.BaseException;
import lombok.Getter;

@Getter
public class AuthException extends BaseException {
	private final String userId;
	private final String attemptedAction;
	private final String clientIp;

	// 기본 생성자들
	public AuthException(ResponseType responseType) {
		super(responseType);
		this.userId = null;
		this.attemptedAction = null;
		this.clientIp = null;
	}

	public AuthException(ResponseType responseType, String customMessage) {
		super(responseType, customMessage);
		this.userId = null;
		this.attemptedAction = null;
		this.clientIp = null;
	}

	public AuthException(ResponseType responseType, Throwable cause) {
		super(responseType, cause);
		this.userId = null;
		this.attemptedAction = null;
		this.clientIp = null;
	}

	public AuthException(ResponseType responseType, String userId, String attemptedAction) {
		super(responseType, String.format("인증 실패: 사용자 %s가 %s 시도", userId, attemptedAction));
		this.userId = userId;
		this.attemptedAction = attemptedAction;
		this.clientIp = null;
	}

	public AuthException(ResponseType responseType, String customMessage, Throwable cause) {
		super(responseType, customMessage, cause);
		this.userId = null;
		this.attemptedAction = null;
		this.clientIp = null;
	}

	// 정적 팩토리 메서드
	public static AuthException unauthorized() {
		return new AuthException(ResponseType.AUTHORIZATION_FAILED);
	}

	public static AuthException unauthorized(String message) {
		return new AuthException(ResponseType.AUTHORIZATION_FAILED, message);
	}

	public static AuthException unauthorized(String userId, String attemptedAction) {
		return new AuthException(ResponseType.AUTHORIZATION_FAILED, userId, attemptedAction);
	}

	public static AuthException forbidden() {
		return new AuthException(ResponseType.DO_NOT_HAVE_PERMISSION);
	}

	public static AuthException forbidden(String message) {
		return new AuthException(ResponseType.DO_NOT_HAVE_PERMISSION, message);
	}

	public static AuthException forbidden(String userId, String attemptedAction) {
		return new AuthException(ResponseType.DO_NOT_HAVE_PERMISSION, userId, attemptedAction);
	}

	public static AuthException loginFailed() {
		return new AuthException(ResponseType.LOGIN_FAILED);
	}

	public static AuthException loginFailed(String message) {
		return new AuthException(ResponseType.LOGIN_FAILED, message);
	}

	public static AuthException loginFailed(Throwable cause) {
		return new AuthException(ResponseType.LOGIN_FAILED, cause);
	}

	// Builder
	private AuthException(Builder builder) {
		super(builder.responseType, builder.customMessage, builder.cause);
		this.userId = builder.userId;
		this.attemptedAction = builder.attemptedAction;
		this.clientIp = builder.clientIp;
	}

	public static Builder builder(ResponseType responseType) {
		return new Builder(responseType);
	}

	public static class Builder {
		private final ResponseType responseType;
		private String customMessage;
		private Throwable cause;
		private String userId;
		private String attemptedAction;
		private String clientIp;

		private Builder(ResponseType responseType) {
			this.responseType = responseType;
		}

		public Builder message(String customMessage) {
			this.customMessage = customMessage;
			return this;
		}

		public Builder cause(Throwable cause) {
			this.cause = cause;
			return this;
		}

		public Builder userId(String userId) {
			this.userId = userId;
			return this;
		}

		public Builder attemptedAction(String attemptedAction) {
			this.attemptedAction = attemptedAction;
			return this;
		}

		public Builder clientIp(String clientIp) {
			this.clientIp = clientIp;
			return this;
		}

		public AuthException build() {
			return new AuthException(this);
		}
	}
}