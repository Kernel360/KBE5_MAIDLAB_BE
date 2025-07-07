package kernel.maidlab.common.dto.reservation.request;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ReservationRequestDto {

	private Long serviceDetailTypeId;

	private String address;
	private String addressDetail;

	private String managerUuid;
	private String housingType;
	private String housingInformation;

	private LocalDateTime reservationDate;
	private LocalDateTime startTime;
	private LocalDateTime endTime;

	private String pet;
	private String specialRequest;

	private Integer lifeCleaningRoomIdx;

	private BigDecimal totalPrice;

	private List<ServiceOptionRequest> serviceOptions; // ENUM 기반으로 해석

	@Data
	@NoArgsConstructor
	public static class ServiceOptionRequest {
		private String id;    // ex) "FAN_CLEANING"
		private Integer count; // null 가능
	}
}
