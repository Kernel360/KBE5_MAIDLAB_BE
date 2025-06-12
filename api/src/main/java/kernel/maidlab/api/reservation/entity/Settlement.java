package kernel.maidlab.api.reservation.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import kernel.maidlab.common.entity.Base;
import kernel.maidlab.common.enums.ServiceType;
import kernel.maidlab.common.enums.Status;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "settlement")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Settlement extends Base {
	@Column(name = "reservation_id", nullable = false)
	private Long reservationId;

	@Column(name = "manager_id", nullable = false)
	private Long managerId;

	@Column(name = "service_detail_type_id", nullable = false)
	private Long serviceDetailTypeId;

	@Enumerated(EnumType.STRING)
	@Column(name = "service_type", nullable = false)
	private ServiceType serviceType;

	@Enumerated(EnumType.STRING)
	@Column(name = "status", nullable = false)
	private Status status;

	@Column(name = "platform_fee", nullable = false)
	private BigDecimal platformFee;

	@Column(name = "amount", nullable = false)
	private BigDecimal amount;

	@Column(name = "create_at", nullable = false)
	private LocalDateTime createdAt;

	@PrePersist
	public void prePersist() {
		if (createdAt == null) {
			this.createdAt = LocalDateTime.now();
		}
	}

	private Settlement(Reservation reservation) {
		this.reservationId = reservation.getId();
		this.managerId = reservation.getManagerId();
		this.serviceDetailTypeId = reservation.getServiceDetailType().getId();
		this.serviceType = reservation.getServiceDetailType().getServiceType();
		this.status = Status.PENDING;
		BigDecimal totalPrice = reservation.getTotalPrice();
		BigDecimal platformFeeRate = new BigDecimal("0.2");
		this.platformFee = totalPrice.multiply(platformFeeRate);
		this.amount = totalPrice.subtract(this.platformFee);

	}

	public static Settlement of(Reservation reservation) {
		return new Settlement(reservation);
	}

	public void approve() {
		this.status = Status.APPROVED;
	}

	public void reject() {
		this.status = Status.REJECTED;
	}
}
