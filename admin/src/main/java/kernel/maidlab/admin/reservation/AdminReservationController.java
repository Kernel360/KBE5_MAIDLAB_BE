package kernel.maidlab.admin.reservation;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import kernel.maidlab.api.reservation.dto.response.AdminWeeklySettlementResponseDto;
import kernel.maidlab.api.reservation.dto.response.ReservationDetailResponseDto;
import kernel.maidlab.api.reservation.dto.response.ReservationResponseDto;
import kernel.maidlab.api.reservation.service.ReservationService;
import kernel.maidlab.common.dto.ResponseDto;
import kernel.maidlab.common.enums.ResponseType;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/reservations")
public class AdminReservationController implements AdminReservationApi {
	private final ReservationService reservationService;

	@GetMapping
	@Override
	public ResponseEntity<ResponseDto<List<ReservationResponseDto>>> adminReservations(HttpServletRequest request , @RequestParam int page, @RequestParam int size) {
		List<ReservationResponseDto> response = reservationService.adminReservations(request , page, size);
		return ResponseDto.success(ResponseType.SUCCESS, response);
	}

	@GetMapping("/{reservationId}")
	@Override
	public ResponseEntity<ResponseDto<ReservationDetailResponseDto>> getReservation(HttpServletRequest request,
		@PathVariable Long reservationId) {
		ReservationDetailResponseDto response = reservationService.getReservationDetail(reservationId, request);
		return ResponseDto.success(ResponseType.SUCCESS, response);
	}

	@GetMapping("/date")
	@Override
	public ResponseEntity<ResponseDto<List<ReservationResponseDto>>> dailyReservations(@RequestParam LocalDate date , @RequestParam int page, @RequestParam int size) {
		List<ReservationResponseDto> response = reservationService.dailyReservations(date , page,  size);
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
		AdminWeeklySettlementResponseDto response = reservationService.getAdminWeeklySettlements(startDate, page, size);
		return ResponseDto.success(ResponseType.SUCCESS, response);
	}



}
