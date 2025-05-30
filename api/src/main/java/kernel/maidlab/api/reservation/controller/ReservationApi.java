package kernel.maidlab.api.reservation.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import kernel.maidlab.api.reservation.dto.request.CheckInOutRequestDto;
import kernel.maidlab.api.reservation.dto.request.ReservationIsApprovedRequestDto;
import kernel.maidlab.api.reservation.dto.request.ReservationRequestDto;
import kernel.maidlab.api.reservation.dto.response.ReservationDetailResponseDto;
import kernel.maidlab.api.reservation.dto.response.ReservationResponseDto;
import kernel.maidlab.common.dto.ResponseDto;

@Tag(name = "Reservation", description = "예약 관련 API")
public interface ReservationApi {
	@Operation(summary = "전체 예약 조회", description = "전체 예약 정보를 조회합니다.", security = @SecurityRequirement(name = "JWT"))
	@ApiResponses({@ApiResponse(responseCode = "200", description = "조회 성공"),
		@ApiResponse(responseCode = "401", description = "비로그인 접속"),
		@ApiResponse(responseCode = "403", description = "권한 없음"),
		@ApiResponse(responseCode = "500", description = "데이터베이스 오류"),})
	ResponseEntity<ResponseDto<List<ReservationResponseDto>>> allReservations(HttpServletRequest request);

	@Operation(summary = "예약 상세 조회", description = "예약 상세 내역 조회", security = @SecurityRequirement(name = "JWT"))
	ResponseEntity<ResponseDto<ReservationDetailResponseDto>> reservationDetail(@PathVariable Long reservationId,
		HttpServletRequest request
	);

	@Operation(summary = "예약 테이블 생성", description = "예약 정보를 받아 예약 테이블을 생성합니다.", security = @SecurityRequirement(name = "JWT"))
	@ApiResponses({@ApiResponse(responseCode = "200", description = "테이블 생성 성공"),
		@ApiResponse(responseCode = "401", description = "비로그인 접속"),
		@ApiResponse(responseCode = "403", description = "권한 없음"),
		@ApiResponse(responseCode = "500", description = "데이터베이스 오류"),})
	ResponseEntity<ResponseDto<String>> create(@RequestBody ReservationRequestDto dto, HttpServletRequest request);

	@Operation(summary = "예약 결제 요청", description = "총 결제 금액이 맞는지 확인합니다.", security = @SecurityRequirement(name = "JWT"))
	@ApiResponses({@ApiResponse(responseCode = "200", description = "올바른 금액입니다."),
		@ApiResponse(responseCode = "400", description = "요금이 다릅니다."),
		@ApiResponse(responseCode = "401", description = "비로그인 접속"),
		@ApiResponse(responseCode = "403", description = "권한 없음"),
		@ApiResponse(responseCode = "500", description = "데이터베이스 오류"),})
	ResponseEntity<ResponseDto<String>> checkPrice(@RequestBody ReservationRequestDto dto);

	@Operation(summary = "예약 요청 응답", description = "매니저의 예약 요청에 대한 응답처리 api입니다. true / false로 요청해야함", security = @SecurityRequirement(name = "JWT"))
	@ApiResponses({@ApiResponse(responseCode = "200", description = "응답 처리 완료"),
		@ApiResponse(responseCode = "401", description = "비로그인 접속"),
		@ApiResponse(responseCode = "403", description = "권한 없음"),
		@ApiResponse(responseCode = "500", description = "데이터베이스 오류"),})
	ResponseEntity<ResponseDto<String>> managerResponseToReservation(@PathVariable Long reservationId,
		@RequestBody ReservationIsApprovedRequestDto dto, HttpServletRequest request);

	@Operation(summary = "Check In", description = "매니저 예약 현장 체크인", security = @SecurityRequirement(name = "JWT"))
	@ApiResponses({@ApiResponse(responseCode = "200", description = "응답 처리 완료"),
		@ApiResponse(responseCode = "401", description = "비로그인 접속"),
		@ApiResponse(responseCode = "403", description = "권한 없음"),
		@ApiResponse(responseCode = "500", description = "데이터베이스 오류"),})
	ResponseEntity<ResponseDto<String>> checkin(@PathVariable Long reservationId, @RequestBody CheckInOutRequestDto dto,
		HttpServletRequest request);

	@Operation(summary = "Check Out", description = "매니저 예약 현장 체크아웃", security = @SecurityRequirement(name = "JWT"))
	@ApiResponses({@ApiResponse(responseCode = "200", description = "응답 처리 완료"),
		@ApiResponse(responseCode = "401", description = "비로그인 접속"),
		@ApiResponse(responseCode = "403", description = "권한 없음"),
		@ApiResponse(responseCode = "500", description = "데이터베이스 오류"),})
	ResponseEntity<ResponseDto<String>> checkout(@PathVariable Long reservationId,
		@RequestBody CheckInOutRequestDto dto, HttpServletRequest request);

	@Operation(summary = "예약 취소", description = "예약 취소 요청", security = @SecurityRequirement(name = "JWT"))
	ResponseEntity<ResponseDto<String>> cancel(@PathVariable Long reservationId, HttpServletRequest request);
}
