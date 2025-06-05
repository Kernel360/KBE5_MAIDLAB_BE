package kernel.maidlab.api.consumer.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import kernel.maidlab.api.consumer.dto.ConsumerMyPageDto;
import kernel.maidlab.api.consumer.dto.request.ConsumerProfileRequestDto;
import kernel.maidlab.api.consumer.dto.request.PreferenceRequestDto;
import kernel.maidlab.api.consumer.dto.response.BlackListedManagerResponseDto;
import kernel.maidlab.api.consumer.dto.response.ConsumerProfileResponseDto;
import kernel.maidlab.api.consumer.dto.response.LikedManagerResponseDto;
import kernel.maidlab.common.dto.ResponseDto;
import lombok.Generated;

@Tag(name = "Consumer", description = "소비자(Consumer) 관련 API")
@Generated
public interface ConsumerApi {

	@Operation(summary = "소비자 프로필 수정", description = "소비자의 프로필 정보를 수정합니다.")
	@ApiResponses(value = {@ApiResponse(responseCode = "200", description = "프로필 수정 성공 (SU)"),
		@ApiResponse(responseCode = "400", description = "Validation failed (VF)"),
		@ApiResponse(responseCode = "401", description = "Authorization failed (AF)"),
		@ApiResponse(responseCode = "500", description = "Database error (DBE)")})
	ResponseEntity<ResponseDto<Void>> updateProfile(HttpServletRequest request,
		@RequestBody(description = "소비자 프로필 수정 DTO", required = true, content = @Content(schema = @Schema(implementation = ConsumerProfileRequestDto.class), examples = @ExampleObject(value = "{\"profileImage\":\"https://image.com/123.jpg\",\"address\":\"서울시 강남구\",\"detailAddress\":\"101동 202호\"}"))) ConsumerProfileRequestDto consumerProfileRequestDto);

	@Operation(summary = "소비자 프로필 조회", description = "소비자의 프로필 정보를 조회합니다.")
	@ApiResponses(value = {@ApiResponse(responseCode = "200", description = "프로필 조회 성공 (SU)"),
		@ApiResponse(responseCode = "401", description = "Authorization failed (AF)"),
		@ApiResponse(responseCode = "500", description = "Database error (DBE)")})
	ResponseEntity<ResponseDto<ConsumerProfileResponseDto>> getProfile(HttpServletRequest request);

	@Operation(summary = "마이페이지 조회", description = "소비자의 마이페이지 정보(이름, 포인트, 프로필 이미지)를 조회합니다.")
	@ApiResponses(value = {@ApiResponse(responseCode = "200", description = "마이페이지 조회 성공 (SU)"),
		@ApiResponse(responseCode = "401", description = "Authorization failed (AF)"),
		@ApiResponse(responseCode = "500", description = "Database error (DBE)")})
	ResponseEntity<ResponseDto<ConsumerMyPageDto>> getMypage(HttpServletRequest request);

	@Operation(summary = "찜한 매니저 조회", description = "소비자가 찜한 매니저 리스트를 조회합니다.")
	@ApiResponses(value = {@ApiResponse(responseCode = "200", description = "조회 성공 (SU)"),
		@ApiResponse(responseCode = "401", description = "Authorization failed (AF)"),
		@ApiResponse(responseCode = "500", description = "Database error (DBE)")})
	ResponseEntity<ResponseDto<List<LikedManagerResponseDto>>> getLikeManagers(HttpServletRequest request);

	@Operation(summary = "블랙리스트 매니저 조회", description = "소비자의 블랙리스트 매니저 정보를 조회합니다.")
	@ApiResponses(value = {@ApiResponse(responseCode = "200", description = "조회 성공 (SU)"),
		@ApiResponse(responseCode = "401", description = "Authorization failed (AF)"),
		@ApiResponse(responseCode = "500", description = "Database error (DBE)")})
	ResponseEntity<ResponseDto<List<BlackListedManagerResponseDto>>> getBlackListManagers(HttpServletRequest request);

	@Operation(summary = "찜/블랙리스트 등록", description = "해당 매니저를 찜 혹은 블랙리스트로 등록합니다.")
	@ApiResponses(value = {@ApiResponse(responseCode = "200", description = "등록 성공 (SU)"),
		@ApiResponse(responseCode = "400", description = "Validation failed (VF)"),
		@ApiResponse(responseCode = "401", description = "Authorization failed (AF)"),
		@ApiResponse(responseCode = "500", description = "Database error (DBE)")})
	ResponseEntity<ResponseDto<Void>> createLikedOrBlackListedManager(HttpServletRequest request, String managerUuid,
		@RequestBody(description = "선호 여부 요청 DTO", required = true, content = @Content(schema = @Schema(implementation = PreferenceRequestDto.class), examples = @ExampleObject(value = "{\"preference\":true}"))) PreferenceRequestDto preferenceRequestDto);

	@Operation(summary = "찜한 매니저 삭제", description = "찜한 매니저를 삭제합니다.")
	@ApiResponses(value = {@ApiResponse(responseCode = "200", description = "삭제 성공 (SU)"),
		@ApiResponse(responseCode = "401", description = "Authorization failed (AF)"),
		@ApiResponse(responseCode = "500", description = "Database error (DBE)")})
	ResponseEntity<ResponseDto<Void>> removeLikedManager(HttpServletRequest request, String managerUuid);
}
