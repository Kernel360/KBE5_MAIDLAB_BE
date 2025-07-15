package kernel.maidlab.domain.reservation.dto.response;

import kernel.maidlab.domain.consumer.entity.Consumer;
import kernel.maidlab.domain.manager.entity.Manager;
import kernel.maidlab.domain.reservation.entity.Reservation;
import kernel.maidlab.common.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class AdminReservationDetailResponseDto {
	private Long id;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;


	private String address;
	private String addressDetail;

	private LocalDateTime startTime;
	private LocalDateTime endTime;
	private LocalDateTime canceledAt;

	private LocalDateTime checkinTime;
	private LocalDateTime checkoutTime;



	private String housingInformation;
	private String housingType;

	private Long managerId;
	private Long consumerId;

	private String pet;
	private LocalDateTime reservationDate;
	private Integer roomSize;
	private String serviceAdd;
	private String specialRequest;
	private Status status;
	private BigDecimal totalPrice;
	private String serviceType;
	private String serviceDetailType;

	//manager 정보

	private String managerPhoneNumber;
	private String managerName;
	private Float managerRate;
	private String managerProfileImage;

	//consumer 정보
	private String consumerPhoneNumber;
	private String consumerName;
	private String consumerProfileImage;

	public static AdminReservationDetailResponseDto getInstance(Long reservationId, Reservation reservation, Manager manager, Consumer consumer) {
		return new AdminReservationDetailResponseDto(
			reservationId,
			reservation.getCreatedAt(),
			reservation.getUpdatedAt(),
			reservation.getAddress(),
			reservation.getAddressDetail(),
			reservation.getStartTime(),
			reservation.getEndTime(),
			reservation.getCanceledAt(),
			reservation.getCheckinTime(),
			reservation.getCheckoutTime(),
			reservation.getHousingInformation(),
			reservation.getHousingType(),
			reservation.getManagerId(),
			reservation.getConsumerId(),
			reservation.getPet(),
			reservation.getReservationDate(),
			reservation.getRoomSize(),
			reservation.getServiceAdd(),
			reservation.getSpecialRequest(),
			reservation.getStatus(),
			reservation.getTotalPrice(),
			reservation.getServiceDetailType().getServiceType().toString(),
			reservation.getServiceDetailType().getServiceDetailType(),
			manager.getPhoneNumber(),
			manager.getName(),
			manager.getAverageRate(),
			manager.getProfileImage(),
			consumer.getPhoneNumber(),
			consumer.getName(),
			consumer.getProfileImage()
		);
	}
}
