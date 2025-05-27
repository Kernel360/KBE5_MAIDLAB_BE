package kernel.maidlab.api.reservation.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import kernel.maidlab.common.enums.ReservationStatus;
import kernel.maidlab.common.entity.Base;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "reservation")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Reservation extends Base {

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "service_detail_type_id", nullable = false)
	private ServiceDetailType serviceDetailType;

	@Column(length = 1000, nullable = false)
	private String address;
	@Column(length = 1000)
	private String addressDetail;

	private Long MatchManagerId;

	@Column(nullable = false)
	private Long consumerId;

	@Column(nullable = false)
	private String housingType;
	@Column(nullable = false)
	private Integer roomSize;
	@Column(nullable = false)
	private String housingInformation;

	@Column(nullable = false)
	private LocalDateTime reservationDate;
	@Column(nullable = false)
	private LocalDateTime startTime;
	@Column(nullable = false)
	private LocalDateTime endTime;

	private String serviceAdd;

	private String pet;

	@Column(columnDefinition = "TEXT")
	private String specialRequest;

	@Column(nullable = false)
	private Long totalPrice;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private ReservationStatus status;

	private LocalDateTime canceledAt;
	private LocalDateTime createdAt;
	private LocalDateTime modifiedAt;

	public void cancel(LocalDateTime canceledAt) {
		this.status = ReservationStatus.CANCELED;
		this.canceledAt = canceledAt;
	}

}
