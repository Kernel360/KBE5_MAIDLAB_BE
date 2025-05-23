package kernel.maidlab.reservation.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import kernel.maidlab.common.dto.ResponseDto;
import kernel.maidlab.reservation.dto.response.AvailableManagerDto;

@Tag(name= "Reservation", description = "예약 관련 API")
public interface ReservationApi {
	@Operation(
		summary = "예약 가능 매니저 리스트 조회",
		description = "예약 가능한 시간에 겹치는 일정이 없는 매니저들을 조회합니다.",
		security = @SecurityRequirement(name = "JWT")
	)
	@ApiResponse(responseCode = "200", description = "조회 성공")
	ResponseEntity<ResponseDto<List<AvailableManagerDto>>> getAvailableManagers(
		@Parameter(description = "예약 시작 시간 (yyyy-MM-ddTHH:mm)", required = true)
		@RequestParam("startTime") LocalDateTime startTime
	);
}
