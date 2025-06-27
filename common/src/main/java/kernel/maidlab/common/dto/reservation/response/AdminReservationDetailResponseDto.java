package kernel.maidlab.common.dto.reservation.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import kernel.maidlab.common.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
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



}
