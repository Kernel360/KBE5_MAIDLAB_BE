package kernel.maidlab.admin.reservation.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import kernel.maidlab.admin.reservation.service.AdminReservationService;
import kernel.maidlab.common.dto.reservation.response.AdminWeeklySettlementResponseDto;
import kernel.maidlab.common.dto.reservation.response.ReservationDetailResponseDto;
import kernel.maidlab.common.dto.reservation.response.ReservationResponseDto;
import kernel.maidlab.common.dto.reservation.response.SettlementResponseDto;
import kernel.maidlab.common.dto.ResponseDto;
import kernel.maidlab.common.enums.ResponseType;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/reservations")
public class AdminReservationController implements AdminReservationApi {
	private final AdminReservationService adminReservationsService;

	@GetMapping
	@Override
	public ResponseEntity<ResponseDto<List<ReservationResponseDto>>> adminReservations(HttpServletRequest request , @RequestParam int page, @RequestParam int size) {
		List<ReservationResponseDto> response = adminReservationsService.adminReservations(request , page, size);
		return ResponseDto.success(ResponseType.SUCCESS, response);
	}

	@GetMapping("/{reservationId}")
	@Override
	public ResponseEntity<ResponseDto<ReservationDetailResponseDto>> getReservation(HttpServletRequest request,
		@PathVariable Long reservationId) {
		ReservationDetailResponseDto response = adminReservationsService.getReservationDetail(reservationId, request);
		return ResponseDto.success(ResponseType.SUCCESS, response);
	}

	@GetMapping("/date")
	@Override
	public ResponseEntity<ResponseDto<List<ReservationResponseDto>>> dailyReservations(@RequestParam LocalDate date , @RequestParam int page, @RequestParam int size) {
		List<ReservationResponseDto> response = adminReservationsService.dailyReservations(date , page,  size);
		return ResponseDto.success(ResponseType.SUCCESS, response);
	}

	// @GetMapping("/settlements")
	// public ResponseEntity<Page<AdminSettlementResponseDto>> getAdminSettlements(
	// 	@RequestParam(defaultValue = "0") int page
	// ) {
	// 	Page<AdminSettlementResponseDto> result = adminSettlementService.getAdminSettlementList(page);
	// 	return ResponseEntity.ok(result);
	// }

	@GetMapping("/settlements/weekly")
	@Override
	public ResponseEntity<ResponseDto<AdminWeeklySettlementResponseDto>> getAdminWeeklySettlements(
		@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate, @RequestParam int page,
		@RequestParam int size) {
		AdminWeeklySettlementResponseDto response = adminReservationsService.getAdminWeeklySettlements(startDate, page, size);
		return ResponseDto.success(ResponseType.SUCCESS, response);
	}

	@GetMapping("settlement/{settlementId}")
	@Override
	public ResponseEntity<ResponseDto<SettlementResponseDto>> getSettlementDetail(HttpServletRequest request,
		@PathVariable Long settlementId){
		SettlementResponseDto response = adminReservationsService.getSettlementDetail(settlementId, request);
		return ResponseDto.success(ResponseType.SUCCESS, response);
	}

	@PatchMapping("settlement/{settlementId}/approve")
	@Override
	public ResponseEntity<ResponseDto<String>> settlementApprove(HttpServletRequest request,
		@PathVariable Long settlementId){
		adminReservationsService.settlementApprove(settlementId);
		return ResponseDto.success("success");
	}

	@PatchMapping("settlement/{settlementId}/reject")
	@Override
	public ResponseEntity<ResponseDto<String>> settlementReject(HttpServletRequest request,
		@PathVariable Long settlementId){
		adminReservationsService.settlementReject(settlementId);
		return ResponseDto.success("success");
	}
}
