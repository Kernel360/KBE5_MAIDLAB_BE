package kernel.maidlab.api.reservation.dto.response;

import java.math.BigDecimal;

import kernel.maidlab.common.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ReservationResponseDto {
	private Long reservationId;
	private Status status;
	private String serviceType;
	private String detailServiceType;
	private String reservationDate;
	private String startTime;
	private String endTime;
	private Boolean isExistReview;
	private BigDecimal totalPrice;
}