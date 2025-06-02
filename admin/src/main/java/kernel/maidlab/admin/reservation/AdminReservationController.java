package kernel.maidlab.admin.reservation;

import java.time.LocalDate;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
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

	@Override
	@GetMapping
	public ResponseEntity<ResponseDto<List<ReservationResponseDto>>> adminReservations(HttpServletRequest request) {
		List<ReservationResponseDto> response = reservationService.adminReservations(request);
		return ResponseDto.success(ResponseType.SUCCESS, response);
	}

	@GetMapping("/date")
	@Override
	public ResponseEntity<ResponseDto<List<ReservationResponseDto>>> dailyReservations(@RequestParam LocalDate date) {
		List<ReservationResponseDto> response = reservationService.dailyReservations(date);
		return ResponseDto.success(ResponseType.SUCCESS, response);
	}
}
