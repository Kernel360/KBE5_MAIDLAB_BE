package kernel.maidlab.reservation.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kernel.maidlab.reservation.dto.request.PaymentRequestDto;
import kernel.maidlab.reservation.service.ReservationService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reservation")
public class ReservationController implements ReservationApi {
	private final ReservationService reservationService;

	@Override
	@PostMapping
	public ResponseEntity<Void> create(
		@RequestBody PaymentRequestDto dto
	) {
		reservationService.createReservation(dto);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

}
