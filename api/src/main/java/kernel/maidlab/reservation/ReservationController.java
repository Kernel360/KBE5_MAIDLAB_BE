package kernel.maidlab.reservation;

import jakarta.servlet.http.HttpServletRequest;
import kernel.maidlab.domain.reservation.dto.request.*;
import kernel.maidlab.domain.reservation.dto.response.ReservationDetailResponseDto;
import kernel.maidlab.domain.reservation.dto.response.ReservationResponseDto;
import kernel.maidlab.domain.reservation.dto.response.WeeklySettlementResponseDto;
import kernel.maidlab.domain.reservation.service.ReservationService;
import kernel.maidlab.common.dto.ResponseDto;
import kernel.maidlab.common.enums.ResponseType;
import kernel.maidlab.common.enums.UserType;
import kernel.maidlab.core.aop.annotation.auth.AuthRequired;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reservations")
public class ReservationController implements ReservationApi {
	private final ReservationService reservationService;

	@Override
	@PostMapping("/payment")
	@AuthRequired(roles = {UserType.CONSUMER})
	public ResponseEntity<ResponseDto<String>> payment(@RequestBody PaymentRequestDto dto, HttpServletRequest request) {
		reservationService.pay(dto, request);
		return ResponseDto.success(ResponseType.SUCCESS, "결제 완료");
	}

	@Override
	@GetMapping
	@AuthRequired(roles = {UserType.CONSUMER, UserType.MANAGER})
	public ResponseEntity<ResponseDto<List<ReservationResponseDto>>> allReservations(HttpServletRequest request) {
		List<ReservationResponseDto> response = reservationService.allReservations(request);
		return ResponseDto.success(ResponseType.SUCCESS, response);
	}

	@GetMapping("/consumer")
	@AuthRequired(roles = {UserType.CONSUMER})
	public ResponseEntity<ResponseDto<org.springframework.data.domain.Page<ReservationResponseDto>>> getConsumerReservationsWithPaging(
		@RequestParam(required = false) String status,
		@RequestParam(defaultValue = "0") int page,
		@RequestParam(defaultValue = "5") int size,
		@RequestParam(defaultValue = "reservationDate") String sortBy,
		@RequestParam(defaultValue = "DESC") String sortOrder,
		HttpServletRequest request) {
		log.info("Get consumer reservations with paging - status: {}, page: {}, size: {}, sortBy: {}, sortOrder: {}",
			status, page, size, sortBy, sortOrder);

		org.springframework.data.domain.Page<ReservationResponseDto> response =
			reservationService.getConsumerReservationsWithPaging(status, page, size, sortBy, sortOrder, request);

		return ResponseDto.success(ResponseType.SUCCESS, response);
	}

	@GetMapping("/manager")
	@AuthRequired(roles = {UserType.MANAGER})
	public ResponseEntity<ResponseDto<org.springframework.data.domain.Page<ReservationResponseDto>>> getManagerReservationsWithPaging(
		@RequestParam(required = false) String status,
		@RequestParam(defaultValue = "0") int page,
		@RequestParam(defaultValue = "5") int size,
		@RequestParam(defaultValue = "DESC") String sortOrder,
		HttpServletRequest request) {
		log.info("Get manager reservations with paging - status: {}, page: {}, size: {}, sortOrder: {}",
			status, page, size, sortOrder);

		org.springframework.data.domain.Page<ReservationResponseDto> response =
			reservationService.getManagerReservationsWithPaging(status, page, size, sortOrder, request);

		return ResponseDto.success(ResponseType.SUCCESS, response);
	}

	@GetMapping("/{reservationId}")
	@Override
	@AuthRequired(roles = {UserType.CONSUMER, UserType.MANAGER})
	public ResponseEntity<ResponseDto<ReservationDetailResponseDto>> reservationDetail(@PathVariable Long reservationId,
																					   HttpServletRequest request) {
		ReservationDetailResponseDto data = reservationService.getReservationDetail(reservationId, request);
		return ResponseDto.success(ResponseType.SUCCESS, data);
	}

	@Override
	@PostMapping("/register")
	@AuthRequired(roles = {UserType.CONSUMER})
	public ResponseEntity<ResponseDto<String>> create(@RequestBody ReservationRequestDto dto,
		HttpServletRequest request) {
		reservationService.createReservation(dto, request);
		String response = "success";
		return ResponseDto.success(ResponseType.SUCCESS, response);
	}

	@Override
	@PostMapping("/price")
	@AuthRequired(roles = {UserType.CONSUMER})
	public ResponseEntity<ResponseDto<String>> checkPrice(@RequestBody ReservationRequestDto dto) {
		reservationService.checkTotalPrice(dto);
		String response = "가격이 맞습니다.";
		return ResponseDto.success(ResponseType.SUCCESS, response);
	}

	@Override
	@PostMapping("/{reservationId}/response")
	@AuthRequired(roles = {UserType.MANAGER})
	public ResponseEntity<ResponseDto<String>> managerResponseToReservation(@PathVariable Long reservationId,
                                                                            @RequestBody ReservationIsApprovedRequestDto dto, HttpServletRequest request) {
		reservationService.managerResponseToReservation(reservationId, dto, request);
		return ResponseDto.success(ResponseType.SUCCESS, "예약 응답 처리가 완료되었습니다.");
	}

	@Override
	@PostMapping("/{reservationId}/checkin")
	@AuthRequired(roles = {UserType.MANAGER})
	public ResponseEntity<ResponseDto<String>> checkin(@PathVariable Long reservationId,
                                                       @RequestBody CheckInOutRequestDto dto, HttpServletRequest request) {
		reservationService.checkin(reservationId, dto, request);
		return ResponseDto.success(ResponseType.SUCCESS, "체크인 완료!");
	}

	@Override
	@PostMapping("/{reservationId}/checkout")
	@AuthRequired(roles = {UserType.MANAGER})
	public ResponseEntity<ResponseDto<String>> checkout(@PathVariable Long reservationId,
		@RequestBody CheckInOutRequestDto dto, HttpServletRequest request) {
		reservationService.checkout(reservationId, dto, request);
		return ResponseDto.success(ResponseType.SUCCESS, "체크아웃 완료!");
	}

	@Override
	@DeleteMapping("/{reservationId}/cancel")
	@AuthRequired(roles = {UserType.CONSUMER, UserType.MANAGER})
	public ResponseEntity<ResponseDto<String>> cancel(@PathVariable Long reservationId, HttpServletRequest request) {
		reservationService.cancel(reservationId, request);
		return ResponseDto.success(ResponseType.SUCCESS, "취소 완료!");
	}

	@Override
	@PostMapping("/review")
	@AuthRequired(roles = {UserType.CONSUMER})
	public ResponseEntity<ResponseDto<String>> review(
            @RequestBody ReviewRegisterRequestDto dto, HttpServletRequest request) {
		reservationService.registerReview(dto, request);
		return ResponseDto.success(ResponseType.SUCCESS, "리뷰 등록 완료!");
	}

	@Override
	@GetMapping("/settlements/weekly-details")
	@AuthRequired(roles = {UserType.MANAGER})
	public ResponseEntity<ResponseDto<WeeklySettlementResponseDto>> getWeeklySettlements(HttpServletRequest request,
																						 @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate) {
		WeeklySettlementResponseDto data = reservationService.getWeeklySettlements(request, startDate);
		return ResponseDto.success(ResponseType.SUCCESS, data);
	}

}
