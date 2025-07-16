package kernel.maidlab.core.exception.custom;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import kernel.maidlab.common.enums.ResponseType;
import kernel.maidlab.core.exception.BaseException;
import lombok.Getter;

@Getter
public class ReservationException extends BaseException {
	private final String reservationId;
	private final String userId;
	private final LocalDateTime requestedDateTime;
	private final String serviceType;
	private final String conflictReason;

	// 기본 생성자들
	public ReservationException(ResponseType responseType) {
		super(responseType);
		this.reservationId = null;
		this.userId = null;
		this.requestedDateTime = null;
		this.serviceType = null;
		this.conflictReason = null;
	}

	public ReservationException(ResponseType responseType, String customMessage) {
		super(responseType, customMessage);
		this.reservationId = null;
		this.userId = null;
		this.requestedDateTime = null;
		this.serviceType = null;
		this.conflictReason = null;
	}

	public ReservationException(ResponseType responseType, String reservationId, String conflictReason) {
		super(responseType, String.format("예약 실패 (ID: %s): %s", reservationId, conflictReason));
		this.reservationId = reservationId;
		this.userId = null;
		this.requestedDateTime = null;
		this.serviceType = null;
		this.conflictReason = conflictReason;
	}

	public ReservationException(ResponseType responseType, String userId, LocalDateTime requestedDateTime,
		String serviceType, String conflictReason) {
		super(responseType, String.format("예약 실패: %s - %s (%s)", serviceType, conflictReason,
			requestedDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))));
		this.reservationId = null;
		this.userId = userId;
		this.requestedDateTime = requestedDateTime;
		this.serviceType = serviceType;
		this.conflictReason = conflictReason;
	}

	// 정적 팩토리 메서드
	public static ReservationException notFound() {
		return new ReservationException(ResponseType.THIS_RESERVATION_DOSE_NOT_EXIST);
	}

	public static ReservationException notFound(String reservationId) {
		return new ReservationException(ResponseType.THIS_RESERVATION_DOSE_NOT_EXIST, reservationId, "예약을 찾을 수 없음");
	}

	public static ReservationException managerNotAvailable() {
		return new ReservationException(ResponseType.AVAILABLE_MANAGER_DOES_NOT_EXIST);
	}

	public static ReservationException managerNotAvailable(LocalDateTime requestedDateTime) {
		return new ReservationException(ResponseType.AVAILABLE_MANAGER_DOES_NOT_EXIST, null, requestedDateTime,
			"청소 서비스", "사용 가능한 매니저가 없음");
	}

	public static ReservationException timeConflict(String userId, LocalDateTime requestedDateTime,
		String serviceType) {
		return new ReservationException(ResponseType.VALIDATION_FAILED, userId, requestedDateTime, serviceType,
			"시간 중복");
	}

	public static ReservationException alreadyCompleted(String reservationId) {
		return new ReservationException(ResponseType.ALREADY_WORKING_OR_COMPLETED, reservationId, "이미 완료된 예약");
	}

	public static ReservationException paymentFailed(String reservationId) {
		return new ReservationException(ResponseType.VALIDATION_FAILED, reservationId, "결제 실패");
	}

	// Builder
	private ReservationException(Builder builder) {
		super(builder.responseType, builder.customMessage, builder.cause);
		this.reservationId = builder.reservationId;
		this.userId = builder.userId;
		this.requestedDateTime = builder.requestedDateTime;
		this.serviceType = builder.serviceType;
		this.conflictReason = builder.conflictReason;
	}

	public static Builder builder(ResponseType responseType) {
		return new Builder(responseType);
	}

	public static class Builder {
		private final ResponseType responseType;
		private String customMessage;
		private Throwable cause;
		private String reservationId;
		private String userId;
		private LocalDateTime requestedDateTime;
		private String serviceType;
		private String conflictReason;

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

		public Builder reservationId(String reservationId) {
			this.reservationId = reservationId;
			return this;
		}

		public Builder userId(String userId) {
			this.userId = userId;
			return this;
		}

		public Builder requestedDateTime(LocalDateTime requestedDateTime) {
			this.requestedDateTime = requestedDateTime;
			return this;
		}

		public Builder serviceType(String serviceType) {
			this.serviceType = serviceType;
			return this;
		}

		public Builder conflictReason(String conflictReason) {
			this.conflictReason = conflictReason;
			return this;
		}

		public ReservationException build() {
			return new ReservationException(this);
		}
	}
}