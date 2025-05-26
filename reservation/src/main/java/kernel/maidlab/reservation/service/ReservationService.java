package kernel.maidlab.reservation.service;

import kernel.maidlab.reservation.dto.request.PaymentRequestDto;

public interface ReservationService {
	// List<AvailableManagerDto> getAvailableManagersList(LocalDateTime startTime);
	void createReservation(PaymentRequestDto dto);
}
