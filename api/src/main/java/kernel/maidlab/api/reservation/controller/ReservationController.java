package kernel.maidlab.api.reservation.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import kernel.maidlab.api.reservation.dto.request.CheckInOutRequestDto;
import kernel.maidlab.api.reservation.dto.request.ReservationIsApprovedRequestDto;
import kernel.maidlab.api.reservation.dto.request.ReservationRequestDto;
import kernel.maidlab.api.reservation.dto.response.ReservationDetailResponseDto;
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
	public ResponseEntity<ResponseDto<List<ReservationResponseDto>>> allReservations(HttpServletRequest request) {
		List<ReservationResponseDto> response = reservationService.allReservations(request);
		return ResponseDto.success(ResponseType.SUCCESS, response);
	}
	@GetMapping("/{reservationId}")
	@Override
	public ResponseEntity<ResponseDto<ReservationDetailResponseDto>> reservationDetail(@PathVariable Long reservationId,
		HttpServletRequest request
	) {
		ReservationDetailResponseDto data = reservationService.getReservationDetail(reservationId, request);
		return ResponseDto.success(ResponseType.SUCCESS,data);
	}

	@Override
	@PostMapping("/register")
	public ResponseEntity<ResponseDto<String>> create(@RequestBody ReservationRequestDto dto,
		HttpServletRequest request) {
		reservationService.createReservation(dto, request);
		String response = "예약 등록이 완료되었습니다.";
		return ResponseDto.success(ResponseType.SUCCESS, response);
	}

	@Override
	@PostMapping("/price")
	public ResponseEntity<ResponseDto<String>> checkPrice(@RequestBody ReservationRequestDto dto) {
		reservationService.checkTotalPrice(dto);
		String response = "가격이 맞습니다.";
		return ResponseDto.success(ResponseType.SUCCESS, response);
	}

	@Override
	@PostMapping("/{reservationId}/response")
	public ResponseEntity<ResponseDto<String>> managerResponseToReservation(@PathVariable Long reservationId,
		@RequestBody ReservationIsApprovedRequestDto dto, HttpServletRequest request) {
		reservationService.managerResponseToReservation(reservationId, dto, request);
		return ResponseDto.success(ResponseType.SUCCESS, "예약 응답 처리가 완료되었습니다.");
	}

	@Override
	@PostMapping("/{reservationId}/checkin")
	public ResponseEntity<ResponseDto<String>> checkin(@PathVariable Long reservationId,
		@RequestBody CheckInOutRequestDto dto, HttpServletRequest request) {
		reservationService.checkin(reservationId, dto, request);
		return ResponseDto.success(ResponseType.SUCCESS, "체크인 완료!");
	}

	@Override
	@PostMapping("/{reservationId}/checkout")
	public ResponseEntity<ResponseDto<String>> checkout(@PathVariable Long reservationId,
		@RequestBody CheckInOutRequestDto dto, HttpServletRequest request) {
		reservationService.checkout(reservationId, dto, request);
		return ResponseDto.success(ResponseType.SUCCESS, "체크아웃 완료!");
	}

	@Override
	@DeleteMapping("/{reservationId}/cancel")
	public ResponseEntity<ResponseDto<String>> cancel(@PathVariable Long reservationId, HttpServletRequest request) {
		reservationService.cancel(reservationId, request);
		return ResponseDto.success(ResponseType.SUCCESS, "취소 완료!");
	}


}
