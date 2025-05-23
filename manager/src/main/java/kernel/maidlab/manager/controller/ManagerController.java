package kernel.maidlab.manager.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import kernel.maidlab.common.dto.FileNamesRequest;
import kernel.maidlab.common.dto.PresignedFileResponse;
import kernel.maidlab.common.dto.baseResponse.BaseResponse;
import kernel.maidlab.common.dto.baseResponse.ResponseCode;
import kernel.maidlab.common.util.JwtUtil;
import kernel.maidlab.manager.dto.ManagerProfile;
import kernel.maidlab.common.service.S3Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Tag(name = "Manager", description = "Manager API")
@RestController
@RequestMapping("/api/managers")
@RequiredArgsConstructor
public class ManagerController {

	private final JwtUtil jwtUtil;
	private final S3Service s3service;

	@GetMapping("/me/profile")
	@Operation(summary = "프로필 조회", description = "JWT 기반 매니저 프로필 조회", security = @SecurityRequirement(name = "JWT"))
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "프로필 조회 성공"),
		@ApiResponse(responseCode = "403", description = "비로그인 접속 및 권한 없음"),
		@ApiResponse(responseCode = "500", description = "DB 오류")
	})
	public ResponseEntity<BaseResponse<ManagerProfile>> getProfile(@RequestHeader("Authorization") String authHeader) {
		String phone = jwtUtil.getPhoneFromToken(authHeader.replace("Bearer ", ""));

		if ("010-1111-1111".equals(phone)) {
			ManagerProfile mockProfile = new ManagerProfile("홍길동", "강남구");
			return ResponseEntity.ok(BaseResponse.success(mockProfile));
		}

		return ResponseEntity.ok(BaseResponse.error(ResponseCode.AF));
	}

	@PostMapping("/me/profile")
	@Operation(summary = "프로필 생성", description = "JWT 기반 매니저 프로필 생성", security = @SecurityRequirement(name = "JWT"))
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "프로필 조회 성공"),
		@ApiResponse(responseCode = "403", description = "비로그인 접속 및 권한 없음"),
		@ApiResponse(responseCode = "500", description = "DB 오류")
	})
	public ResponseEntity<BaseResponse<String>> createProfile(
		@RequestHeader("Authorization") String authHeader,
		@RequestBody ManagerProfile managerProfile) {

		String phone = jwtUtil.getPhoneFromToken(authHeader.replace("Bearer ", ""));

		if ("010-1111-1111".equals(phone)) {
			return ResponseEntity.ok(BaseResponse.success("프로필 생성 완료"));
		}

		return ResponseEntity.ok(BaseResponse.error(ResponseCode.AF));
	}

	//매니저 프로필 이미지 저장 테스트 api
	@PostMapping("/me/fileupload")
	public ResponseEntity<?> getPresignedUploadUrls(@RequestBody FileNamesRequest request) {
		List<String> filenames = request.getFilenames();
		List<PresignedFileResponse> presignedUrls = s3service.uploadFile(filenames, "profile_image/");
		return ResponseEntity.ok(BaseResponse.success(presignedUrls));
	}

}
