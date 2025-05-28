package kernel.maidlab.api.reservation.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ReservationResponseDto {
	private String serviceType;
	private String detailServiceType;
	private String reservationDate;
	private String startTime;
	private String endTime;
	private Long totalPrice;
}