package kernel.maidlab.api.reservation.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import kernel.maidlab.common.dto.reservation.request.PaymentRequestDto;
import kernel.maidlab.common.dto.reservation.request.CheckInOutRequestDto;
import kernel.maidlab.common.dto.reservation.request.ReservationIsApprovedRequestDto;
import kernel.maidlab.common.dto.reservation.request.ReservationRequestDto;
import kernel.maidlab.common.dto.reservation.request.ReviewRegisterRequestDto;
import kernel.maidlab.common.dto.reservation.response.ReservationDetailResponseDto;
import kernel.maidlab.common.dto.reservation.response.ReservationResponseDto;
import kernel.maidlab.common.dto.reservation.response.WeeklySettlementResponseDto;
import kernel.maidlab.api.reservation.service.ReservationService;
import kernel.maidlab.common.dto.ResponseDto;
import kernel.maidlab.common.enums.ResponseType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reservations")
public class ReservationController implements ReservationApi {
	private final ReservationService reservationService;

	@Override
	@PostMapping("/payment")
	public ResponseEntity<ResponseDto<String>> payment(@RequestBody PaymentRequestDto dto, HttpServletRequest request){
		reservationService.pay(dto, request);
		return ResponseDto.success(ResponseType.SUCCESS, "결제 완료");
	}

	@Override
	@GetMapping
	public ResponseEntity<ResponseDto<List<ReservationResponseDto>>> allReservations(HttpServletRequest request) {
		log.info("Get all reservations request received");
		List<ReservationResponseDto> response = reservationService.allReservations(request);
		return ResponseDto.success(ResponseType.SUCCESS, response);
	}

	@GetMapping("/{reservationId}")
	@Override
	public ResponseEntity<ResponseDto<ReservationDetailResponseDto>> reservationDetail(@PathVariable Long reservationId,
		HttpServletRequest request) {
		log.info("Get reservation detail request received for reservationId: {}", reservationId);
		ReservationDetailResponseDto data = reservationService.getReservationDetail(reservationId, request);
		return ResponseDto.success(ResponseType.SUCCESS, data);
	}

	@Override
	@PostMapping("/register")
	public ResponseEntity<ResponseDto<String>> create(@RequestBody ReservationRequestDto dto,
		HttpServletRequest request) {
		log.info("Create reservation request received");
		reservationService.createReservation(dto, request);
		String response = "success";
		return ResponseDto.success(ResponseType.SUCCESS, response);
	}

	@Override
	@PostMapping("/price")
	public ResponseEntity<ResponseDto<String>> checkPrice(@RequestBody ReservationRequestDto dto) {
		log.info("Check price request received");
		reservationService.checkTotalPrice(dto);
		String response = "가격이 맞습니다.";
		return ResponseDto.success(ResponseType.SUCCESS, response);
	}

	@Override
	@PostMapping("/{reservationId}/response")
	public ResponseEntity<ResponseDto<String>> managerResponseToReservation(@PathVariable Long reservationId,
		@RequestBody ReservationIsApprovedRequestDto dto, HttpServletRequest request) {
		log.info("Manager response to reservation request received for reservationId: {}", reservationId);
		reservationService.managerResponseToReservation(reservationId, dto, request);
		return ResponseDto.success(ResponseType.SUCCESS, "예약 응답 처리가 완료되었습니다.");
	}

	@Override
	@PostMapping("/{reservationId}/checkin")
	public ResponseEntity<ResponseDto<String>> checkin(@PathVariable Long reservationId,
		@RequestBody CheckInOutRequestDto dto, HttpServletRequest request) {
		log.info("Checkin request received for reservationId: {}", reservationId);
		reservationService.checkin(reservationId, dto, request);
		return ResponseDto.success(ResponseType.SUCCESS, "체크인 완료!");
	}

	@Override
	@PostMapping("/{reservationId}/checkout")
	public ResponseEntity<ResponseDto<String>> checkout(@PathVariable Long reservationId,
		@RequestBody CheckInOutRequestDto dto, HttpServletRequest request) {
		log.info("Checkout request received for reservationId: {}", reservationId);
		reservationService.checkout(reservationId, dto, request);
		return ResponseDto.success(ResponseType.SUCCESS, "체크아웃 완료!");
	}

	@Override
	@DeleteMapping("/{reservationId}/cancel")
	public ResponseEntity<ResponseDto<String>> cancel(@PathVariable Long reservationId, HttpServletRequest request) {
		log.info("Cancel reservation request received for reservationId: {}", reservationId);
		reservationService.cancel(reservationId, request);
		return ResponseDto.success(ResponseType.SUCCESS, "취소 완료!");
	}

	@Override
	@PostMapping("/{reservationId}/review")
	public ResponseEntity<ResponseDto<String>> review(@PathVariable Long reservationId,
		@RequestBody ReviewRegisterRequestDto dto, HttpServletRequest request) {
		log.info("Register review request received for reservationId: {}", reservationId);
		reservationService.registerReview(reservationId, dto, request);
		return ResponseDto.success(ResponseType.SUCCESS, "리뷰 등록 완료!");
	}

	@Override
	@GetMapping("/settlements/weekly-details")
	public ResponseEntity<ResponseDto<WeeklySettlementResponseDto>> getWeeklySettlements(HttpServletRequest request,
		@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate) {
		log.info("Get weekly settlements request received for startDate: {}", startDate);
		WeeklySettlementResponseDto data = reservationService.getWeeklySettlements(request, startDate);
		return ResponseDto.success(ResponseType.SUCCESS, data);
	}

}
