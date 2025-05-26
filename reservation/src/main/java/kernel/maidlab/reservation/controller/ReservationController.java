package kernel.maidlab.reservation.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kernel.maidlab.common.dto.ResponseDto;
import kernel.maidlab.common.dto.ResponseType;
import kernel.maidlab.reservation.dto.request.ReservationRequestDto;
import kernel.maidlab.reservation.service.ReservationService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reservations")
public class ReservationController implements ReservationApi {
	private final ReservationService reservationService;

	@Override
	@PostMapping("/match")
	public ResponseEntity<ResponseDto<String>> create(
		@RequestBody ReservationRequestDto dto
	) {
		reservationService.createReservation(dto);
		String response = "예약 등록이 완료되었습니다.";
		return ResponseDto.success(ResponseType.SUCCESS, response);
	}

	@Override
	@PostMapping("/price")
	public ResponseEntity<ResponseDto<String>> checkPrice(
		@RequestBody ReservationRequestDto dto
	) {
		reservationService.checkTotalPrice(dto);
		String response = "가격이 맞습니다.";
		return ResponseDto.success(ResponseType.SUCCESS, response);
	}

}
