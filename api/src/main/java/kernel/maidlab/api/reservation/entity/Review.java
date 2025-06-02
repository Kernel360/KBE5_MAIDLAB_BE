package kernel.maidlab.api.reservation.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import kernel.maidlab.api.reservation.dto.request.ReviewRegisterRequestDto;
import kernel.maidlab.common.entity.Base;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "review")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Review extends Base {
	@Column(name = "reservation_id", nullable = false)
	private Long reservationId;
	@Column(name = "manager_id", nullable = false)
	private Long managerId;
	@Column(name = "consumer_id", nullable = false)
	private Long consumerId;
	@Column(name = "rating", nullable = false)
	private float rating;
	@Column(name = "comment", nullable = false)
	private String comment;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "service_detail_type_id", nullable = false)
	private ServiceDetailType serviceDetailType;

	@Column(name = "review_date", nullable = false)
	private LocalDateTime reviewDate;

	@Column(name = "is_consumer_to_manager", nullable = false)
	private Boolean isConsumerToManager;


	private Review(Long reservationId, Long managerId, Long consumerId, float rating, String comment,
		ServiceDetailType serviceType, Boolean isConsumerToManager ) {
		this.reservationId = reservationId;
		this.managerId = managerId;
		this.consumerId = consumerId;
		this.rating = rating;
		this.comment = comment;
		this.serviceDetailType = serviceType;
		this.isConsumerToManager = isConsumerToManager;
	}
	@PrePersist
	public void prePersist() {
		if (reviewDate == null) {
			this.reviewDate = LocalDateTime.now();
		}
	}

	public static Review of(ReviewRegisterRequestDto dto, Reservation reservation, Boolean isConsumerToManager) {
		return new Review(reservation.getId(), reservation.getManagerId(), reservation.getConsumerId(),
			dto.getRating(), dto.getComment(), reservation.getServiceDetailType(), isConsumerToManager);
	}
}
