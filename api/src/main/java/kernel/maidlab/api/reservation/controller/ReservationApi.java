package kernel.maidlab.api.reservation.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import kernel.maidlab.common.dto.reservation.request.PaymentRequestDto;
import kernel.maidlab.common.dto.reservation.request.CheckInOutRequestDto;
import kernel.maidlab.common.dto.reservation.request.ReservationIsApprovedRequestDto;
import kernel.maidlab.common.dto.reservation.request.ReservationRequestDto;
import kernel.maidlab.common.dto.reservation.request.ReviewRegisterRequestDto;
import kernel.maidlab.common.dto.reservation.response.ReservationDetailResponseDto;
import kernel.maidlab.common.dto.reservation.response.ReservationResponseDto;
import kernel.maidlab.common.dto.reservation.response.WeeklySettlementResponseDto;
import kernel.maidlab.common.dto.ResponseDto;
import lombok.Generated;

@Tag(name = "Reservation", description = "예약(Reservation) 관련 API")
@Generated
public interface ReservationApi {

	@Operation(summary = "전체 예약 조회", description = "사용자의 모든 예약을 조회합니다.", security = @SecurityRequirement(name = "JWT"))
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "Success (SU)", content = @Content(array = @ArraySchema(schema = @Schema(implementation = ReservationResponseDto.class)))),
		@ApiResponse(responseCode = "401", description = "Authorization failed / Login failed (AF | LF)"),
		@ApiResponse(responseCode = "403", description = "Do not have permission (NP)"),
		@ApiResponse(responseCode = "500", description = "Database error (DBE)")
	})
	ResponseEntity<ResponseDto<List<ReservationResponseDto>>> allReservations(HttpServletRequest request);

	@Operation(summary = "예약 상세 조회", description = "예약 ID로 상세 예약 정보를 조회합니다.", security = @SecurityRequirement(name = "JWT"))
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "Success (SU)", content = @Content(schema = @Schema(implementation = ReservationDetailResponseDto.class))),
		@ApiResponse(responseCode = "404", description = "Reservation not found (NR)"),
		@ApiResponse(responseCode = "401", description = "Authorization failed / Login failed (AF | LF)"),
		@ApiResponse(responseCode = "403", description = "Do not have permission (NP)"),
		@ApiResponse(responseCode = "500", description = "Database error (DBE)")
	})
	ResponseEntity<ResponseDto<ReservationDetailResponseDto>> reservationDetail(@PathVariable Long reservationId,
		HttpServletRequest request);

	@Operation(summary = "예약 생성", description = "예약 정보를 등록합니다.", security = @SecurityRequirement(name = "JWT"))
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "Success (SU)"),
		@ApiResponse(responseCode = "400", description = "Validation failed (VF) | Duplicate tel number (DT) | Duplicate reservation (DR) | Wrong address (WR)"),
		@ApiResponse(responseCode = "401", description = "Authorization failed / Login failed (AF | LF)"),
		@ApiResponse(responseCode = "403", description = "Do not have permission (NP)"),
		@ApiResponse(responseCode = "500", description = "Database error (DBE)")
	})
	ResponseEntity<ResponseDto<String>> create(@RequestBody ReservationRequestDto dto,HttpServletRequest request);

	@Operation(summary = "결제 금액 확인", description = "총 결제 금액 검증 API.", security = @SecurityRequirement(name = "JWT"))
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "Success (SU) – 금액 일치"),
		@ApiResponse(responseCode = "400", description = "Validation failed (VF) – 금액 불일치"),
		@ApiResponse(responseCode = "401", description = "Authorization failed / Login failed (AF | LF)"),
		@ApiResponse(responseCode = "403", description = "Do not have permission (NP)"),
		@ApiResponse(responseCode = "500", description = "Database error (DBE)")
	})
	ResponseEntity<ResponseDto<String>> checkPrice(@RequestBody ReservationRequestDto dto);

	@Operation(summary = "예약 요청 응답", description = "매니저가 예약 수락/거절 응답.", security = @SecurityRequirement(name = "JWT"))
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "Success (SU) – 응답 완료"),
		@ApiResponse(responseCode = "400", description = "Already working or completed (AWC)"),
		@ApiResponse(responseCode = "404", description = "Reservation not found (NR)"),
		@ApiResponse(responseCode = "401", description = "Authorization failed / Login failed (AF | LF)"),
		@ApiResponse(responseCode = "403", description = "Do not have permission (NP)"),
		@ApiResponse(responseCode = "500", description = "Database error (DBE)")
	})
	ResponseEntity<ResponseDto<String>> managerResponseToReservation(@PathVariable Long reservationId,
		@RequestBody ReservationIsApprovedRequestDto dto, HttpServletRequest request);

	@Operation(summary = "결제", description = "임시 결제 로직", security = @SecurityRequirement(name = "JWT"))
	ResponseEntity<ResponseDto<String>> payment(@RequestBody PaymentRequestDto dto, HttpServletRequest request);

	@Operation(summary = "Check-In", description = "현장 체크인 API", security = @SecurityRequirement(name = "JWT"))
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "Success (SU) – 체크인 완료"),
		@ApiResponse(responseCode = "400", description = "Already checked in (AC)"),
		@ApiResponse(responseCode = "404", description = "Reservation not found (NR)"),
		@ApiResponse(responseCode = "401", description = "Authorization failed / Login failed (AF | LF)"),
		@ApiResponse(responseCode = "403", description = "Do not have permission (NP)"),
		@ApiResponse(responseCode = "500", description = "Database error (DBE)")
	})
	ResponseEntity<ResponseDto<String>> checkin(@PathVariable Long reservationId, @RequestBody CheckInOutRequestDto dto,
		HttpServletRequest request);

	@Operation(summary = "Check-Out", description = "현장 체크아웃 API", security = @SecurityRequirement(name = "JWT"))
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "Success (SU) – 체크아웃 완료"),
		@ApiResponse(responseCode = "400", description = "Already checked out (AC)"),
		@ApiResponse(responseCode = "404", description = "Reservation not found (NR)"),
		@ApiResponse(responseCode = "401", description = "Authorization failed / Login failed (AF | LF)"),
		@ApiResponse(responseCode = "403", description = "Do not have permission (NP)"),
		@ApiResponse(responseCode = "500", description = "Database error (DBE)")
	})
	ResponseEntity<ResponseDto<String>> checkout(@PathVariable Long reservationId,
		@RequestBody CheckInOutRequestDto dto, HttpServletRequest request);

	@Operation(summary = "예약 취소", description = "예약 취소 요청 API", security = @SecurityRequirement(name = "JWT"))
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "Success (SU) – 예약 취소 완료"),
		@ApiResponse(responseCode = "404", description = "Reservation not found (NR)"),
		@ApiResponse(responseCode = "401", description = "Authorization failed / Login failed (AF | LF)"),
		@ApiResponse(responseCode = "403", description = "Do not have permission (NP)"),
		@ApiResponse(responseCode = "500", description = "Database error (DBE)")
	})
	ResponseEntity<ResponseDto<String>> cancel(@PathVariable Long reservationId, HttpServletRequest request);

	@Operation(summary = "리뷰 등록", description = "예약 리뷰 등록 API", security = @SecurityRequirement(name = "JWT"))
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "Success (SU) – 리뷰 등록 완료"),
		@ApiResponse(responseCode = "404", description = "Reservation not found (NR)"),
		@ApiResponse(responseCode = "401", description = "Authorization failed / Login failed (AF | LF)"),
		@ApiResponse(responseCode = "403", description = "Do not have permission (NP)"),
		@ApiResponse(responseCode = "500", description = "Database error (DBE)")
	})
	ResponseEntity<ResponseDto<String>> review(
		@RequestBody ReviewRegisterRequestDto dto, HttpServletRequest request);

	@Operation(summary = "주간 정산 조회", description = "매니저 주간 정산 정보 조회 API", security = @SecurityRequirement(name = "JWT"))
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "Success (SU)", content = @Content(schema = @Schema(implementation = WeeklySettlementResponseDto.class))),
		@ApiResponse(responseCode = "401", description = "Authorization failed / Login failed (AF | LF)"),
		@ApiResponse(responseCode = "403", description = "Do not have permission (NP)"),
		@ApiResponse(responseCode = "500", description = "Database error (DBE)")
	})
	ResponseEntity<ResponseDto<WeeklySettlementResponseDto>> getWeeklySettlements(HttpServletRequest request,
		@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate);

	@Operation(summary = "소비자 예약 조회 (페이징)", description = "소비자의 예약을 페이징, 필터링, 정렬하여 조회합니다. MATCHED 상태가 PAID보다 우선순위가 높습니다.", security = @SecurityRequirement(name = "JWT"))
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "Success (SU)", content = @Content(schema = @Schema(implementation = Page.class))),
		@ApiResponse(responseCode = "400", description = "Validation failed (VF) - 잘못된 파라미터"),
		@ApiResponse(responseCode = "401", description = "Authorization failed / Login failed (AF | LF)"),
		@ApiResponse(responseCode = "403", description = "Do not have permission (NP)"),
		@ApiResponse(responseCode = "500", description = "Database error (DBE)")
	})
	ResponseEntity<ResponseDto<Page<ReservationResponseDto>>> getConsumerReservationsWithPaging(
		@RequestParam(required = false) String status,
		@RequestParam(defaultValue = "0") int page,
		@RequestParam(defaultValue = "5") int size,
		@RequestParam(defaultValue = "reservationDate") String sortBy,
		@RequestParam(defaultValue = "DESC") String sortOrder,
		HttpServletRequest request);

	ResponseEntity<ResponseDto<org.springframework.data.domain.Page<ReservationResponseDto>>> getManagerReservationsWithPaging(
			@RequestParam(required = false) String status,
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "5") int size,
			@RequestParam(defaultValue = "DESC") String sortOrder,
			HttpServletRequest request);
}
