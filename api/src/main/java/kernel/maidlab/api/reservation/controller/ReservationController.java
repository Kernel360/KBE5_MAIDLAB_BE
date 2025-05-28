package kernel.maidlab.api.reservation.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kernel.maidlab.api.reservation.dto.request.ReservationIsApprovedRequestDto;
import kernel.maidlab.api.reservation.dto.request.ReservationRequestDto;
import kernel.maidlab.api.reservation.dto.response.ReservationResponseDto;
import kernel.maidlab.api.reservation.service.ReservationService;
import kernel.maidlab.common.dto.ResponseDto;
import kernel.maidlab.common.enums.ResponseType;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reservations")
public class ReservationController implements ReservationApi {
	private final ReservationService reservationService;

	@Override
	@GetMapping
	public ResponseEntity<ResponseDto<List<ReservationResponseDto>>> allReservations() {
		List<ReservationResponseDto> response = reservationService.allReservations();
		return ResponseDto.success(ResponseType.SUCCESS, response);
	}

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

	@Override
	@PostMapping("/{reservationId}/response")
	public ResponseEntity<ResponseDto<String>> managerResponseToReservation(
		@PathVariable Long reservationId,
		@RequestBody ReservationIsApprovedRequestDto dto
	) {
		Long managerId = 100L; // TODO : managerId jwt 토큰에서 받기
		reservationService.managerResponseToReservation(reservationId, dto, managerId);
		return ResponseDto.success(ResponseType.SUCCESS, "예약 응답 처리가 완료되었습니다.");
	}

}
