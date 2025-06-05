package kernel.maidlab.api.manager.controller;

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
import kernel.maidlab.api.manager.dto.request.*;
import kernel.maidlab.api.manager.dto.response.*;
import kernel.maidlab.common.dto.ResponseDto;
import lombok.Generated;

@Tag(name = "Manager", description = "매니저(Manager) 관련 API")
@Generated
public interface ManagerApi {

	@Operation(summary = "매니저 프로필 생성", description = "매니저가 최초 로그인 후 제공하는 기본 프로필 정보를 생성합니다. 서비스 지역, 가능 시간, 서비스 항목, 자기소개 등 입력이 포함됩니다.")
	@ApiResponses(value = {@ApiResponse(responseCode = "200", description = "프로필 생성 성공 (SU)"),
		@ApiResponse(responseCode = "400", description = "Validation failed (VF)"),
		@ApiResponse(responseCode = "401", description = "Authorization failed (AF)"),
		@ApiResponse(responseCode = "500", description = "Database error (DBE)")})
	ResponseEntity<ResponseDto<Void>> createProfile(
		@RequestBody(description = "매니저 프로필 생성 DTO", required = true, content = @Content(schema = @Schema(implementation = ProfileRequestDto.class), examples = @ExampleObject(value = "{\"profileImage\":\"https://image.com/manager.jpg\",\"introduceText\":\"경력 5년의 전문가입니다.\"}"))) ProfileRequestDto req,
		HttpServletRequest httpReq);

	@Operation(summary = "매니저 마이페이지 조회", description = "매니저의 간단한 마이페이지 정보(이름, 프로필 이미지, 승인 여부)를 조회합니다.")
	@ApiResponses(value = {@ApiResponse(responseCode = "200", description = "마이페이지 조회 성공 (SU)"),
		@ApiResponse(responseCode = "401", description = "Authorization failed (AF)"),
		@ApiResponse(responseCode = "500", description = "Database error (DBE)")})
	ResponseEntity<ResponseDto<MypageResponseDto>> getMypage(HttpServletRequest req);

	@Operation(summary = "매니저 프로필 상세 조회", description = "매니저의 상세 프로필 정보(이름, 성별, 생년월일, 지역, 시간, 서비스 항목 등)를 조회합니다.")
	@ApiResponses(value = {@ApiResponse(responseCode = "200", description = "프로필 조회 성공 (SU)"),
		@ApiResponse(responseCode = "401", description = "Authorization failed (AF)"),
		@ApiResponse(responseCode = "500", description = "Database error (DBE)")})
	ResponseEntity<ResponseDto<ProfileResponseDto>> getProfile(HttpServletRequest req);

	@Operation(summary = "매니저 프로필 수정", description = "기존 프로필 정보를 수정합니다. 수정 시 지역, 서비스 항목, 시간, 자기소개 등을 새롭게 등록하며 기존 데이터는 삭제됩니다.")
	@ApiResponses(value = {@ApiResponse(responseCode = "200", description = "프로필 수정 성공 (SU)"),
		@ApiResponse(responseCode = "400", description = "Validation failed (VF)"),
		@ApiResponse(responseCode = "401", description = "Authorization failed (AF)"),
		@ApiResponse(responseCode = "500", description = "Database error (DBE)")})
	ResponseEntity<ResponseDto<Void>> updateProfile(
		@RequestBody(description = "매니저 프로필 수정 DTO", required = true, content = @Content(schema = @Schema(implementation = ProfileUpdateRequestDto.class), examples = @ExampleObject(value = "{\"name\":\"홍길동\",\"birth\":\"1990-01-01\",\"gender\":\"MALE\",\"profileImage\":\"https://image.com/profile.jpg\"}"))) ProfileUpdateRequestDto req,
		HttpServletRequest httpReq);

	@Operation(summary = "매니저 리뷰 목록 조회", description = "매니저가 받은 모든 리뷰 목록을 조회합니다. 별점, 코멘트, 서비스 유형 등이 포함됩니다.")
	@ApiResponses(value = {@ApiResponse(responseCode = "200", description = "리뷰 조회 성공 (SU)"),
		@ApiResponse(responseCode = "401", description = "Authorization failed (AF)"),
		@ApiResponse(responseCode = "500", description = "Database error (DBE)")})
	ResponseEntity<ResponseDto<ReviewListResponseDto>> getMyReviews(HttpServletRequest req);
}


