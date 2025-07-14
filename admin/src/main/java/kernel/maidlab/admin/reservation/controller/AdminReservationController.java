package kernel.maidlab.admin.reservation.controller;

import java.math.BigDecimal;
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
import kernel.maidlab.common.dto.ResponseDto;
import kernel.maidlab.common.dto.reservation.response.AdminReservationDetailResponseDto;
import kernel.maidlab.common.dto.reservation.response.AdminWeeklySettlementResponseDto;
import kernel.maidlab.common.dto.reservation.response.ReservationResponseDto;
import kernel.maidlab.common.dto.reservation.response.SettlementGraphDataDto;
import kernel.maidlab.common.dto.reservation.response.SettlementResponseDto;
import kernel.maidlab.common.enums.ResponseType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/reservations")
@Slf4j
public class AdminReservationController implements AdminReservationApi {
	private final AdminReservationService adminReservationsService;

	@GetMapping
	@Override
	public ResponseEntity<ResponseDto<List<ReservationResponseDto>>> adminReservations(HttpServletRequest request,
		@RequestParam int page, @RequestParam int size) {
		List<ReservationResponseDto> response = adminReservationsService.adminReservations(request, page, size);
		return ResponseDto.success(ResponseType.SUCCESS, response);
	}

	@GetMapping("/{reservationId}")
	@Override
	public ResponseEntity<ResponseDto<AdminReservationDetailResponseDto>> getReservation(HttpServletRequest request,
		@PathVariable Long reservationId) {
		AdminReservationDetailResponseDto response = adminReservationsService.getReservationDetail(reservationId,
			request);
		return ResponseDto.success(ResponseType.SUCCESS, response);
	}

	@GetMapping("/date")
	@Override
	public ResponseEntity<ResponseDto<List<ReservationResponseDto>>> dailyReservations(@RequestParam LocalDate date,
		@RequestParam int page, @RequestParam int size) {
		List<ReservationResponseDto> response = adminReservationsService.dailyReservations(date, page, size);
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
		AdminWeeklySettlementResponseDto response = adminReservationsService.getAdminWeeklySettlements(startDate, page,
			size);
		return ResponseDto.success(ResponseType.SUCCESS, response);
	}

	@GetMapping("settlement/{settlementId}")
	@Override
	public ResponseEntity<ResponseDto<SettlementResponseDto>> getSettlementDetail(HttpServletRequest request,
		@PathVariable Long settlementId) {
		SettlementResponseDto response = adminReservationsService.getSettlementDetail(settlementId, request);
		return ResponseDto.success(ResponseType.SUCCESS, response);
	}

	@PatchMapping("settlement/{settlementId}/approve")
	@Override
	public ResponseEntity<ResponseDto<String>> settlementApprove(HttpServletRequest request,
		@PathVariable Long settlementId) {
		adminReservationsService.settlementApprove(settlementId);
		return ResponseDto.success("success");
	}

	@PatchMapping("settlement/{settlementId}/reject")
	@Override
	public ResponseEntity<ResponseDto<String>> settlementReject(HttpServletRequest request,
		@PathVariable Long settlementId) {
		adminReservationsService.settlementReject(settlementId);
		return ResponseDto.success("success");
	}

	@GetMapping("/todayreservation")
	@Override
	public ResponseEntity<ResponseDto<Long>> todayReservation(HttpServletRequest request) {
		return ResponseDto.success(adminReservationsService.getTodayReservation(request));
	}

	@GetMapping("/consumer/{id}")
	@Override
	public ResponseEntity<ResponseDto<List<ReservationResponseDto>>> consumerReservation(HttpServletRequest request,
		@PathVariable Long id, @RequestParam int page, @RequestParam int size) {
		return ResponseDto.success(adminReservationsService.getConsumerReservation(request, id, page, size));
	}

	@GetMapping("/manager/{id}")
	@Override
	public ResponseEntity<ResponseDto<List<ReservationResponseDto>>> managerReservation(HttpServletRequest request,
		@PathVariable Long id, @RequestParam int page, @RequestParam int size) {
		return ResponseDto.success(adminReservationsService.getManagerReservation(request, id, page, size));
	}

	@GetMapping("/reservationcount/{consumerId}")
	@Override
	public ResponseEntity<ResponseDto<Long>> reservationCount(HttpServletRequest request,
		@PathVariable Long consumerId) {
		return ResponseDto.success(ResponseType.SUCCESS,
			adminReservationsService.getCountByConsumerId(request, consumerId));
	}

	@GetMapping("/totalpaidmoney/{consumerId}")
	@Override
	public ResponseEntity<ResponseDto<BigDecimal>> totalPaidMoney(HttpServletRequest request,
		@PathVariable Long consumerId) {
		return ResponseDto.success(ResponseType.SUCCESS,
			adminReservationsService.getTotalPaidMoney(request, consumerId));
	}

	@GetMapping("/reviewedpercent/{consumerId}")
	@Override
	public ResponseEntity<ResponseDto<BigDecimal>> consumerReviewedPercent(HttpServletRequest request,
		@PathVariable Long consumerId) {
		return ResponseDto.success(ResponseType.SUCCESS,
			adminReservationsService.getReviewedPercent(request, consumerId));
	}

	@GetMapping("/matchedcount/{managerId}")
	@Override
	public ResponseEntity<ResponseDto<Long>> managerActiveReservationCount(HttpServletRequest request,
		@PathVariable Long managerId) {
		return ResponseDto.success(ResponseType.SUCCESS,
			adminReservationsService.getActiveReservationCountByManagerId(request, managerId));
	}

	@GetMapping("/manager/settlementsum/{managerId}")
	@Override
	public ResponseEntity<ResponseDto<BigDecimal>> managerSettlementSum(HttpServletRequest request,
		@PathVariable Long managerId) {
		return ResponseDto.success(ResponseType.SUCCESS,
			adminReservationsService.getTotalSettlementAmountByManagerId(request, managerId));
	}

	@GetMapping("/managerreviewedpercent/{managerId}")
	@Override
	public ResponseEntity<ResponseDto<BigDecimal>> managerReviewedPercent(HttpServletRequest request,
		@PathVariable Long managerId) {
		return ResponseDto.success(ResponseType.SUCCESS,
			adminReservationsService.getManagerReviewedPercent(request, managerId));
	}

	@GetMapping("/settlements/graph")
	@Override
	public ResponseEntity<ResponseDto<SettlementGraphDataDto>> getSettlementGraphData(
		HttpServletRequest request,
		@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
		@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
		@RequestParam(required = false, defaultValue = "DAILY") String period) {
		SettlementGraphDataDto response = adminReservationsService.getSettlementGraphData(request, startDate, endDate,
			period);
		return ResponseDto.success(ResponseType.SUCCESS, response);
	}

}
