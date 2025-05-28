package kernel.maidlab.api.reservation.service;

import java.util.List;

import kernel.maidlab.api.reservation.dto.request.ReservationRequestDto;
import kernel.maidlab.api.reservation.dto.response.ReservationResponseDto;

public interface ReservationService {
	void createReservation(ReservationRequestDto dto);
	List<ReservationResponseDto>  allReservations();
	void checkTotalPrice(ReservationRequestDto dto);
}
