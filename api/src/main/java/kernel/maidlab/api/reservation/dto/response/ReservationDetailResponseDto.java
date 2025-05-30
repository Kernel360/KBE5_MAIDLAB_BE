package kernel.maidlab.api.reservation.dto.response;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReservationDetailResponseDto {
	private String serviceType;
	private String serviceDetailType;

	private String address;
	private String addressDetail;

	private String managerUuId;
	private String managerName;
	private String managerProfileImageUrl;
	private Float managerAverageRate;
	private List<String> managerRegion;
	private String managerPhoneNumber;

	private String housingType;
	private Integer roomSize;
	private String housingInformation;

	private LocalDateTime reservationDate;
	private LocalDateTime startTime;
	private LocalDateTime endTime;

	private String serviceAdd;
	private String pet;

	private String specialRequest;

	private Long totalPrice;

}
