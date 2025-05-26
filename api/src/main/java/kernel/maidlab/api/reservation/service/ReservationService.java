package kernel.maidlab.api.reservation.service;

import kernel.maidlab.api.reservation.dto.request.ReservationRequestDto;

public interface ReservationService {
	void createReservation(ReservationRequestDto dto);

	void checkTotalPrice(ReservationRequestDto dto);
}
