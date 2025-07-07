package kernel.maidlab.admin.reservation.controller;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import kernel.maidlab.common.dto.reservation.response.AdminReservationDetailResponseDto;
import kernel.maidlab.common.dto.reservation.response.AdminWeeklySettlementResponseDto;
import kernel.maidlab.common.dto.reservation.response.ReservationResponseDto;
import kernel.maidlab.common.dto.reservation.response.SettlementResponseDto;
import kernel.maidlab.common.dto.reservation.response.SettlementGraphDataDto;
import kernel.maidlab.common.dto.ResponseDto;

@Tag(name = "Reservation", description = "예약 관련 API")
public interface AdminReservationApi {
	@Operation(summary = "전체 예약 조회", description = "전체 예약 정보를 조회합니다.", security = @SecurityRequirement(name = "JWT"))
	@ApiResponses({@ApiResponse(responseCode = "200", description = "조회 성공"),
		@ApiResponse(responseCode = "401", description = "비로그인 접속"),
		@ApiResponse(responseCode = "403", description = "권한 없음"),
		@ApiResponse(responseCode = "500", description = "데이터베이스 오류"),})
	ResponseEntity<ResponseDto<List<ReservationResponseDto>>> adminReservations(HttpServletRequest request,
		@RequestParam int page, @RequestParam int size);

	@GetMapping("/{reservationId}")
	ResponseEntity<ResponseDto<AdminReservationDetailResponseDto>> getReservation(HttpServletRequest request,
		@PathVariable Long reservationId);

	@Operation(summary = "일별 예약 조회", description = "지정된 날짜별 예약 정보를 조회합니다.", security = @SecurityRequirement(name = "JWT"))
	@ApiResponses({@ApiResponse(responseCode = "200", description = "조회 성공"),
		@ApiResponse(responseCode = "401", description = "비로그인 접속"),
		@ApiResponse(responseCode = "403", description = "권한 없음"),
		@ApiResponse(responseCode = "500", description = "데이터베이스 오류"),})
	ResponseEntity<ResponseDto<List<ReservationResponseDto>>> dailyReservations(@RequestParam LocalDate date,
		@RequestParam int page, @RequestParam int size);

	@Operation(summary = "주간 정산 조회", description = "admin용 매니저들의 주간 정산 조회", security = @SecurityRequirement(name = "JWT"))
	ResponseEntity<ResponseDto<AdminWeeklySettlementResponseDto>> getAdminWeeklySettlements(
		@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate, @RequestParam int page,
		@RequestParam int size);


	@Operation(summary = "정산 상세 조회", description = "정산 id에 따른 상세 정보를 조회합니다.", security = @SecurityRequirement(name = "JWT"))
	@ApiResponses({@ApiResponse(responseCode = "200", description = "조회 성공"),
		@ApiResponse(responseCode = "401", description = "비로그인 접속"),
		@ApiResponse(responseCode = "403", description = "권한 없음"),
		@ApiResponse(responseCode = "500", description = "데이터베이스 오류"),})
	@GetMapping("settlement/{settlementId}")
	ResponseEntity<ResponseDto<SettlementResponseDto>> getSettlementDetail(HttpServletRequest request,
		@PathVariable Long settlementId);



	@PatchMapping("settlement/{settlementId}/approve")
	@Operation(summary = "정산 승인", description = "정산 승인 API")
	@ApiResponses(value = {@ApiResponse(responseCode = "200", description = "계정 승인 완료 (SU)"),
		@ApiResponse(responseCode = "401", description = "Authorization failed (AF)"),
		@ApiResponse(responseCode = "500", description = "Database error (DBE)")})
	ResponseEntity<ResponseDto<String>> settlementApprove(HttpServletRequest request, @PathVariable Long settlementId);

	@PatchMapping("settlement/{settlementId}/reject")
	@Operation(summary = "정산 거부", description = "정산 거부 API")
	@ApiResponses(value = {@ApiResponse(responseCode = "200", description = "계정 승인 완료 (SU)"),
		@ApiResponse(responseCode = "401", description = "Authorization failed (AF)"),
		@ApiResponse(responseCode = "500", description = "Database error (DBE)")})
	ResponseEntity<ResponseDto<String>> settlementReject(HttpServletRequest request,
		@PathVariable Long settlementId);

	@GetMapping("/todayreservation")
	@Operation(summary = "오늘의 예약 수 조회", description = "오늘 예약 수 조회 API")
	@ApiResponses(value = {@ApiResponse(responseCode = "200", description = "조회 완료 (SU)"),
		@ApiResponse(responseCode = "401", description = "Authorization failed (AF)"),
		@ApiResponse(responseCode = "500", description = "Database error (DBE)")})
	ResponseEntity<ResponseDto<Long>> todayReservation(HttpServletRequest request);



	@GetMapping("/consumer/{id}")
	ResponseEntity<ResponseDto<List<ReservationResponseDto>>> consumerReservation(HttpServletRequest request,
		@PathVariable Long id, @RequestParam int page, @RequestParam int size);

	@GetMapping("/manager/{id}")
	ResponseEntity<ResponseDto<List<ReservationResponseDto>>> managerReservation(HttpServletRequest request,
		@PathVariable Long id, @RequestParam int page, @RequestParam int size);

	@GetMapping("/reservationcount/{consumerId}")
	ResponseEntity<ResponseDto<Long>> reservationCount(HttpServletRequest request, @PathVariable Long consumerId);

	@GetMapping("/totalpaidmoney/{consumerId}")
	ResponseEntity<ResponseDto<BigDecimal>> totalPaidMoney(HttpServletRequest request, @PathVariable Long consumerId);

	@GetMapping("/reviewedpercent/{consumerId}")
	ResponseEntity<ResponseDto<BigDecimal>> consumerReviewedPercent(HttpServletRequest request, @PathVariable Long consumerId);

	@GetMapping("/manager/activecount/{managerId}")
	@Operation(summary = "매니저의 활성 예약 수 조회", description = "매니저의 PAID, WORKING, COMPLETED 상태 예약 수를 조회합니다.", security = @SecurityRequirement(name = "JWT"))
	@ApiResponses({@ApiResponse(responseCode = "200", description = "조회 성공"),
		@ApiResponse(responseCode = "401", description = "비로그인 접속"),
		@ApiResponse(responseCode = "403", description = "권한 없음"),
		@ApiResponse(responseCode = "500", description = "데이터베이스 오류")})
	ResponseEntity<ResponseDto<Long>> managerActiveReservationCount(HttpServletRequest request, @PathVariable Long managerId);

	@GetMapping("/manager/settlementsum/{managerId}")
	@Operation(summary = "매니저의 승인된 정산 금액 총합 조회", description = "매니저 ID에 해당하는 승인된(APPROVED) 정산 금액의 합계를 조회합니다.", security = @SecurityRequirement(name = "JWT"))
	@ApiResponses({@ApiResponse(responseCode = "200", description = "조회 성공"),
		@ApiResponse(responseCode = "401", description = "비로그인 접속"),
		@ApiResponse(responseCode = "403", description = "권한 없음"),
		@ApiResponse(responseCode = "500", description = "데이터베이스 오류")})
	ResponseEntity<ResponseDto<BigDecimal>> managerSettlementSum(HttpServletRequest request, @PathVariable Long managerId);

	@GetMapping("/managerreviewedpercent/{managerId}")
	ResponseEntity<ResponseDto<BigDecimal>> managerReviewedPercent(HttpServletRequest request,
		@PathVariable Long consumerId);

	@GetMapping("/settlements/graph")
	@Operation(summary = "정산 그래프 데이터 조회", description = "관리자 대시보드용 정산 그래프 데이터를 조회합니다.", security = @SecurityRequirement(name = "JWT"))
	@ApiResponses({@ApiResponse(responseCode = "200", description = "조회 성공"),
		@ApiResponse(responseCode = "401", description = "비로그인 접속"),
		@ApiResponse(responseCode = "403", description = "권한 없음"),
		@ApiResponse(responseCode = "500", description = "데이터베이스 오류")})
	ResponseEntity<ResponseDto<SettlementGraphDataDto>> getSettlementGraphData(
		HttpServletRequest request,
		@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
		@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
		@RequestParam(required = false, defaultValue = "DAILY") String period);
}
