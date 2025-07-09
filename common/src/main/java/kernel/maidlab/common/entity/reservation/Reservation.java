package kernel.maidlab.common.entity.reservation;

import jakarta.persistence.*;
import kernel.maidlab.common.dto.reservation.request.PaymentRequestDto;
import kernel.maidlab.common.dto.reservation.request.ReservationRequestDto;
import kernel.maidlab.common.entity.base.TimeBase;
import kernel.maidlab.common.entity.consumer.Consumer;
import kernel.maidlab.common.entity.point.Point;
import kernel.maidlab.common.enums.Status;
import kernel.maidlab.common.util.ReservationOptionUtil;
import kernel.maidlab.common.util.RoomSizeRuleUtil;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "reservation")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Reservation extends TimeBase {
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
	private BigDecimal totalPrice;

	@Column(name = "status", nullable = false)
	@Enumerated(EnumType.STRING)
	private Status status;

	@Column(name = "canceled_at")
	private LocalDateTime canceledAt;

	@Column(name = "checkin_time")
	private LocalDateTime checkinTime;

	@Column(name = "checkout_time")
	private LocalDateTime checkoutTime;

	public void pay(){
		this.status = Status.PAID;
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

	public void managerRespondApproved(Long managerId) {
		this.managerId = managerId;
		this.status = Status.MATCHED;
	}

	public void managerRespondRejected(Long managerId) {
		this.managerId = managerId;
		this.status = Status.REJECTED;
	}

	private Reservation(Long managerId, Long consumerId, ServiceDetailType serviceDetailType,
		LocalDateTime reservationDate, LocalDateTime startTime, LocalDateTime endTime, String address,
		String addressDetail, String housingType, Integer roomSize, String housingInformation, String serviceAdd,
		String pet, String specialRequest, BigDecimal totalPrice) {
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

	public static Reservation of(ReservationRequestDto dto, Long consumerId, Long managerId,
		ServiceDetailType detailType) {
		Integer roomSize = RoomSizeRuleUtil.resolveRoomSize(dto.getLifeCleaningRoomIdx());
		String serializedOptions = ReservationOptionUtil.serializeOptions(dto.getServiceOptions());
		return new Reservation(
				managerId,
				consumerId,
				detailType,
				dto.getReservationDate(),
				dto.getStartTime(),
				dto.getEndTime(),
				dto.getAddress(),
				dto.getAddressDetail(),
				dto.getHousingType(),
				roomSize,
				dto.getHousingInformation(),
				serializedOptions,
				dto.getPet(),
				dto.getSpecialRequest(),
				dto.getTotalPrice());
	}

}
