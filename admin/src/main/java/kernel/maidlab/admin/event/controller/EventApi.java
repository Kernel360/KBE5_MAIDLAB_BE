package kernel.maidlab.admin.event.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import kernel.maidlab.admin.event.dto.request.EventRequestDto;
import kernel.maidlab.admin.event.dto.response.EventListResponseDto;
import kernel.maidlab.admin.event.dto.response.EventResponseDto;
import kernel.maidlab.common.dto.ResponseDto;
import lombok.Generated;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

@Tag(name = "AdminEvent", description = "관리자 이벤트(Admin Event) 관련 API")
@Generated
public interface EventApi {

	@Operation(summary = "이벤트 전체 조회", description = "등록된 전체 이벤트 목록을 최신순으로 조회합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "이벤트 목록 조회 성공 (SU)"),
		@ApiResponse(responseCode = "500", description = "데이터베이스 오류 (DBE)")
	})
	ResponseEntity<ResponseDto<EventListResponseDto>> getAllEvents();

	@Operation(summary = "이벤트 상세 조회", description = "특정 이벤트 ID로 상세 정보를 조회합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "이벤트 조회 성공 (SU)"),
		@ApiResponse(responseCode = "404", description = "존재하지 않는 이벤트 (NR)"),
		@ApiResponse(responseCode = "500", description = "데이터베이스 오류 (DBE)")
	})
	ResponseEntity<ResponseDto<EventResponseDto>> getEventById(@PathVariable Long eventId);

	@Operation(summary = "이벤트 생성", description = "관리자가 신규 이벤트를 생성합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "이벤트 생성 성공 (SU)"),
		@ApiResponse(responseCode = "400", description = "입력값 검증 실패 (VF)"),
		@ApiResponse(responseCode = "401", description = "인증 실패 (AF)"),
		@ApiResponse(responseCode = "500", description = "데이터베이스 오류 (DBE)")
	})
	ResponseEntity<ResponseDto<Void>> createEvent(
		@RequestBody(description = "이벤트 생성 DTO", required = true, content = @Content(
			schema = @Schema(implementation = EventRequestDto.class),
			examples = @ExampleObject(value = "{\"title\":\"여름 이벤트\",\"mainImageUrl\":\"https://img.com/main.jpg\",\"imageUrl\":\"https://img.com/detail.jpg\",\"content\":\"이벤트 상세 내용입니다.\"}")))
		EventRequestDto eventRequestDto,
		HttpServletRequest req);

	@Operation(summary = "이벤트 수정", description = "이벤트 ID를 통해 기존 이벤트를 수정합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "이벤트 수정 성공 (SU)"),
		@ApiResponse(responseCode = "400", description = "입력값 검증 실패 (VF)"),
		@ApiResponse(responseCode = "401", description = "인증 실패 (AF)"),
		@ApiResponse(responseCode = "404", description = "존재하지 않는 이벤트 (NR)"),
		@ApiResponse(responseCode = "500", description = "데이터베이스 오류 (DBE)")
	})
	ResponseEntity<ResponseDto<Void>> updateEvent(
		@PathVariable Long eventId,
		@RequestBody(description = "이벤트 수정 DTO", required = true, content = @Content(
			schema = @Schema(implementation = EventRequestDto.class),
			examples = @ExampleObject(value = "{\"title\":\"가을 이벤트\",\"mainImageUrl\":\"https://img.com/newmain.jpg\",\"imageUrl\":\"https://img.com/newdetail.jpg\",\"content\":\"수정된 내용입니다.\"}")))
		EventRequestDto eventRequestDto,
		HttpServletRequest req);

	@Operation(summary = "이벤트 삭제", description = "이벤트 ID를 통해 특정 이벤트를 삭제합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "이벤트 삭제 성공 (SU)"),
		@ApiResponse(responseCode = "401", description = "인증 실패 (AF)"),
		@ApiResponse(responseCode = "404", description = "존재하지 않는 이벤트 (NR)"),
		@ApiResponse(responseCode = "500", description = "데이터베이스 오류 (DBE)")
	})
	ResponseEntity<ResponseDto<Void>> deleteEvent(@PathVariable Long eventId, HttpServletRequest req);

}