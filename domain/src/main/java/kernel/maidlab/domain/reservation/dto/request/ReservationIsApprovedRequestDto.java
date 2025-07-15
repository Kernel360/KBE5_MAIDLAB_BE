package kernel.maidlab.domain.reservation.dto.request;

import lombok.Getter;

@Getter
public class ReservationIsApprovedRequestDto {
	private Boolean status; // true : approved, false : rejected
}
