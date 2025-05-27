package kernel.maidlab.api.reservation.entity;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import kernel.maidlab.api.reservation.dto.request.ReservationRequestDto;
import kernel.maidlab.common.enums.ReservationStatus;
import kernel.maidlab.common.entity.Base;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "reservation")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Reservation extends Base {
	@Column(name = "manager_id")
	private Long managerId;

	@Column(name = "consumer_id",nullable = false)
	private Long consumerId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "service_detail_type_id", nullable = false)
	private ServiceDetailType serviceDetailType;


	@Column(name="reservation_date", nullable = false)
	private LocalDateTime reservationDate;
	@Column(name="start_time",nullable = false)
	private LocalDateTime startTime;
	@Column(name="end_time", nullable = false)
	private LocalDateTime endTime;


	@Column(name="address", length = 1000, nullable = false)
	private String address;
	@Column(name="address_detail", length = 1000)
	private String addressDetail;


	@Column(name="housing_type", nullable = false)
	private String housingType;
	@Column(name="room_size", nullable = false)
	private Integer roomSize;
	@Column(name="housing_information")
	private String housingInformation;

	@Column(name="service_add")
	private String serviceAdd;
	@Column(name="pet")
	private String pet;

	@Column(name="special_request",columnDefinition = "TEXT")
	private String specialRequest;

	@Column(name="total_price", nullable = false)
	private Long totalPrice;

	@Column(name="status", nullable = false)
	@Enumerated(EnumType.STRING)
	private ReservationStatus status;
	@Column(name="is_repeat")
	private Boolean isRepeat;
	@Column(name="repeat_count")
	private Integer repeatCount;

	@Column(name="canceled_at")
	private LocalDateTime canceledAt;

	@CreatedDate
	@Column(name="created_at", nullable = false)
	private LocalDateTime createdAt;

	@LastModifiedDate
	@Column(name="modified_at", nullable = false)
	private LocalDateTime modifiedAt;

	public void cancel(LocalDateTime canceledAt) {
		this.status = ReservationStatus.CANCELED;
		this.canceledAt = canceledAt;
	}

	private Reservation(
		Long managerId,
		Long consumerId,
		ServiceDetailType serviceDetailType,
		LocalDateTime reservationDate,
		LocalDateTime startTime,
		LocalDateTime endTime,
		String address,
		String addressDetail,
		String housingType,
		Integer roomSize,
		String housingInformation,
		String serviceAdd,
		String pet,
		String specialRequest,
		Long totalPrice,
		Boolean isRepeat,
		Integer repeatCount
	) {
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
		this.status = ReservationStatus.PENDING;
		this.isRepeat = isRepeat;
		this.repeatCount = repeatCount;
	}

	public static Reservation of(ReservationRequestDto dto, ServiceDetailType detailType) {
		return new Reservation(
			dto.getManagerId(),
			dto.getConsumerId(),
			detailType,
			dto.getReservationDate(),
			dto.getStartTime(),
			dto.getEndTime(),
			dto.getAddress(),
			dto.getAddressDetail(),
			dto.getHousingType(),
			dto.getRoomSize(),
			dto.getHousingInformation(),
			dto.getServiceAdd(),
			dto.getPet(),
			dto.getSpecialRequest(),
			dto.getTotalPrice(),
			dto.getIsRepeat(),
			dto.getRepeatCount()
		);
	}

}
