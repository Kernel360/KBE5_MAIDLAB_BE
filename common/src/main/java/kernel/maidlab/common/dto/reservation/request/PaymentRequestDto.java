package kernel.maidlab.common.dto.reservation.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class PaymentRequestDto {
	private Long reservationId;
	private Integer pointToUse;
	private boolean pointUsed;
}
