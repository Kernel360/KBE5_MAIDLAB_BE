package kernel.maidlab.api.reservation.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import kernel.maidlab.api.reservation.dto.request.ReservationRequestDto;
import kernel.maidlab.common.entity.Base;
import kernel.maidlab.common.enums.Status;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "reservation")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Reservation extends Base {
	@Column(name = "manager_id")
	private Long managerId;

	@Column(name = "consumer_id", nullable = false)
	private Long consumerId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "service_detail_type_id", nullable = false)
	private ServiceDetailType serviceDetailType;

	@Column(name = "reservation_date", nullable = false)
	private LocalDateTime reservationDate;
	@Column(name = "start_time", nullable = false)
	private LocalDateTime startTime;
	@Column(name = "end_time", nullable = false)
	private LocalDateTime endTime;

	@Column(name = "address", length = 1000, nullable = false)
	private String address;
	@Column(name = "address_detail", length = 1000)
	private String addressDetail;

	@Column(name = "housing_type", nullable = false)
	private String housingType;
	@Column(name = "room_size", nullable = false)
	private Integer roomSize;
	@Column(name = "housing_information")
	private String housingInformation;

	@Column(name = "service_add")
	private String serviceAdd;
	@Column(name = "pet")
	private String pet;

	@Column(name = "special_request", columnDefinition = "TEXT")
	private String specialRequest;

	@Column(name = "total_price", nullable = false)
	private Long totalPrice;

	@Column(name = "status", nullable = false)
	@Enumerated(EnumType.STRING)
	private Status status;

	@Column(name = "canceled_at")
	private LocalDateTime canceledAt;

	@Column(name = "created_at", nullable = false)
	private LocalDateTime createdAt;

	@Column(name = "modified_at", nullable = false)
	private LocalDateTime modifiedAt;

	@Column(name = "checkin_time")
	private LocalDateTime checkinTime;

	@Column(name = "checkout_time")
	private LocalDateTime checkoutTime;

	@PrePersist
	public void prePersist() {
		if (createdAt == null) {
			this.createdAt = LocalDateTime.now();
		}
		if (modifiedAt == null) {
			this.modifiedAt = LocalDateTime.now();
		}
	}

	public void checkin(LocalDateTime checkinTime) {
		this.checkinTime = checkinTime;
		this.status = Status.WORKING;
	}

	public void checkout(LocalDateTime checkoutTime) {
		this.checkoutTime = checkoutTime;
		this.status = Status.COMPLETED;
	}

	public void cancel(LocalDateTime canceledAt) {
		this.canceledAt = canceledAt;
		this.status = Status.CANCELED;
	}

	public void managerRespond(Long managerId) {
		this.managerId = managerId;
		this.status = Status.MATCHED;
	}

	private Reservation(Long managerId, Long consumerId, ServiceDetailType serviceDetailType,
		LocalDateTime reservationDate, LocalDateTime startTime, LocalDateTime endTime, String address,
		String addressDetail, String housingType, Integer roomSize, String housingInformation, String serviceAdd,
		String pet, String specialRequest, Long totalPrice) {
		this.managerId = managerId;
		this.consumerId = consumerId;
		this.serviceDetailType = serviceDetailType;
		this.reservationDate = reservationDate;
		this.startTime = startTime;
		this.endTime = endTime;
		this.address = address;
		this.addressDetail = addressDetail;
		this.housingType = housingType;
		this.roomSize = roomSize;
		this.housingInformation = housingInformation;
		this.serviceAdd = serviceAdd;
		this.pet = pet;
		this.specialRequest = specialRequest;
		this.totalPrice = totalPrice;
		this.status = Status.PENDING;
	}

	public static Reservation of(ReservationRequestDto dto, Long consumerId, ServiceDetailType detailType) {
		return new Reservation(dto.getManagerId(), consumerId, detailType, dto.getReservationDate(), dto.getStartTime(),
			dto.getEndTime(), dto.getAddress(), dto.getAddressDetail(), dto.getHousingType(), dto.getRoomSize(),
			dto.getHousingInformation(), dto.getServiceAdd(), dto.getPet(), dto.getSpecialRequest(),
			dto.getTotalPrice());
	}

}
