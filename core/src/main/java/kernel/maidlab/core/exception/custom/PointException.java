package kernel.maidlab.core.exception.custom;

import kernel.maidlab.common.enums.ResponseType;
import kernel.maidlab.core.exception.BaseException;
import lombok.Getter;

@Getter
public class PointException extends BaseException {
	private final String userId;
	private final Integer requestedPoints;
	private final Integer availablePoints;
	private final String operation;

	// 기본 생성자들
	public PointException(ResponseType responseType) {
		super(responseType);
		this.userId = null;
		this.requestedPoints = null;
		this.availablePoints = null;
		this.operation = null;
	}

	public PointException(ResponseType responseType, String customMessage) {
		super(responseType, customMessage);
		this.userId = null;
		this.requestedPoints = null;
		this.availablePoints = null;
		this.operation = null;
	}

	public PointException(ResponseType responseType, String userId, int requestedPoints, int availablePoints) {
		super(responseType, String.format("포인트 부족: 요청 %d, 보유 %d", requestedPoints, availablePoints));
		this.userId = userId;
		this.requestedPoints = requestedPoints;
		this.availablePoints = availablePoints;
		this.operation = null;
	}

	public PointException(ResponseType responseType, String userId, int requestedPoints, int availablePoints,
		String operation) {
		super(responseType, String.format("포인트 %s 실패: 요청 %d, 보유 %d", operation, requestedPoints, availablePoints));
		this.userId = userId;
		this.requestedPoints = requestedPoints;
		this.availablePoints = availablePoints;
		this.operation = operation;
	}

	// 정적 팩토리 메서드
	public static PointException insufficient() {
		return new PointException(ResponseType.INSUFFICIENT_POINT);
	}

	public static PointException insufficient(String message) {
		return new PointException(ResponseType.INSUFFICIENT_POINT, message);
	}

	public static PointException insufficient(String userId, int requestedPoints, int availablePoints) {
		return new PointException(ResponseType.INSUFFICIENT_POINT, userId, requestedPoints, availablePoints);
	}

	public static PointException usageFailed(String userId, int requestedPoints, int availablePoints) {
		return new PointException(ResponseType.INSUFFICIENT_POINT, userId, requestedPoints, availablePoints, "사용");
	}

	public static PointException earnFailed(String userId, int points) {
		return new PointException(ResponseType.DATABASE_ERROR, userId, points, 0, "적립");
	}

	// Builder
	private PointException(Builder builder) {
		super(builder.responseType, builder.customMessage, builder.cause);
		this.userId = builder.userId;
		this.requestedPoints = builder.requestedPoints;
		this.availablePoints = builder.availablePoints;
		this.operation = builder.operation;
	}

	public static Builder builder(ResponseType responseType) {
		return new Builder(responseType);
	}

	public static class Builder {
		private final ResponseType responseType;
		private String customMessage;
		private Throwable cause;
		private String userId;
		private Integer requestedPoints;
		private Integer availablePoints;
		private String operation;

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

		public Builder requestedPoints(Integer requestedPoints) {
			this.requestedPoints = requestedPoints;
			return this;
		}

		public Builder availablePoints(Integer availablePoints) {
			this.availablePoints = availablePoints;
			return this;
		}

		public Builder operation(String operation) {
			this.operation = operation;
			return this;
		}

		public PointException build() {
			return new PointException(this);
		}
	}
}