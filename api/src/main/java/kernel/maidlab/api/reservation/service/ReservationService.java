package kernel.maidlab.api.reservation.service;


import kernel.maidlab.api.reservation.dto.request.ReservationRequestDto;


public interface ReservationService {
	// List<AvailableManagerDto> getAvailableManagersList(LocalDateTime startTime);
	void createReservation(ReservationRequestDto dto);

	void checkTotalPrice(ReservationRequestDto dto);
}
