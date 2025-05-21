package kernel.maidlab.manager.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import kernel.maidlab.common.dto.baseResponse.BaseResponse;
import kernel.maidlab.common.dto.baseResponse.ResponseCode;
import kernel.maidlab.common.util.JwtUtil;
import kernel.maidlab.manager.dto.ManagerProfile;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Manager", description = "Manager API")
@RestController
@RequestMapping("/api/managers")
@RequiredArgsConstructor
public class ManagerController {

	private final JwtUtil jwtUtil;

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
}
