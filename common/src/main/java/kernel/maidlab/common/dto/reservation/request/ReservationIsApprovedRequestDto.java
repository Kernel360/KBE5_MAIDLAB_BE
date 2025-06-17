package kernel.maidlab.common.dto.reservation.request;

import lombok.Getter;

@Getter
public class ReservationIsApprovedRequestDto {
	private Boolean status; // true : approved, false : rejected
}
