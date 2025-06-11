package kernel.maidlab.api.auth.controller;

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
import jakarta.servlet.http.HttpServletResponse;
import kernel.maidlab.api.auth.dto.request.*;
import kernel.maidlab.api.auth.dto.response.*;
import kernel.maidlab.common.dto.ResponseDto;
import lombok.Generated;

@Tag(name = "Auth", description = "인증(Auth) 관련 API")
@Generated
public interface AuthApi {

	@Operation(summary = "일반 회원가입", description = "유저 유형, 전화번호, 비밀번호, 이름, 생년월일, 성별 정보를 받아 회원가입을 처리합니다.")
	@ApiResponses(value = {@ApiResponse(responseCode = "200", description = "회원가입 성공 (SU)"),
		@ApiResponse(responseCode = "400", description = "Validation failed (VF), Duplicate tel number (DT)"),
		@ApiResponse(responseCode = "500", description = "Database error (DBE)")})
	ResponseEntity<ResponseDto<Void>> signUp(
		@RequestBody(description = "회원가입 요청 DTO", required = true, content = @Content(schema = @Schema(implementation = SignUpRequestDto.class), examples = @ExampleObject(value = "{\"userType\":\"USER\",\"phoneNumber\":\"01012345678\",\"password\":\"abcd1234\",\"name\":\"홍길동\",\"birth\":\"1995-05-10\",\"gender\":\"MALE\"}"))) SignUpRequestDto req);

	@Operation(summary = "일반 로그인", description = "전화번호와 비밀번호를 기반으로 로그인을 수행합니다.")
	@ApiResponses(value = {@ApiResponse(responseCode = "200", description = "로그인 성공 (SU)"),
		@ApiResponse(responseCode = "401", description = "Login failed (LF), Account deleted (AD)"),
		@ApiResponse(responseCode = "500", description = "Database error (DBE)")})
	ResponseEntity<ResponseDto<LoginResponseDto>> login(
		@RequestBody(description = "로그인 요청 DTO", required = true, content = @Content(schema = @Schema(implementation = LoginRequestDto.class), examples = @ExampleObject(value = "{\"userType\":\"USER\",\"phoneNumber\":\"01012345678\",\"password\":\"abcd1234\"}"))) LoginRequestDto req,
		HttpServletResponse res);

	@Operation(summary = "소셜 로그인", description = "소셜 유형 및 유저 유형을 기반으로 소셜 로그인을 수행합니다.")
	@ApiResponses(value = {@ApiResponse(responseCode = "200", description = "소셜 로그인 성공 (SU)"),
		@ApiResponse(responseCode = "401", description = "Login failed (LF), Account deleted (AD)"),
		@ApiResponse(responseCode = "500", description = "Database error (DBE)")})
	ResponseEntity<ResponseDto<SocialLoginResponseDto>> socialLogin(
		@RequestBody(description = "소셜 로그인 요청 DTO", required = true, content = @Content(schema = @Schema(implementation = SocialLoginRequestDto.class), examples = @ExampleObject(value = "{\"userType\":\"USER\",\"socialType\":\"KAKAO\",\"code\":\"authcode123\"}"))) SocialLoginRequestDto req,
		HttpServletRequest request, HttpServletResponse res);

	@Operation(summary = "소셜 회원가입", description = "소셜 로그인 후 필수정보인 생년월일과 성별을 받아 회원가입을 완료합니다.")
	@ApiResponses(value = {@ApiResponse(responseCode = "200", description = "소셜 회원가입 성공 (SU)"),
		@ApiResponse(responseCode = "400", description = "Validation failed (VF)"),
		@ApiResponse(responseCode = "500", description = "Database error (DBE)")})
	ResponseEntity<ResponseDto<Void>> socialSignUp(
		@RequestBody(description = "소셜 회원가입 요청 DTO", required = true, content = @Content(schema = @Schema(implementation = SocialSignUpRequestDto.class), examples = @ExampleObject(value = "{\"birth\":\"1990-01-01\",\"gender\":\"FEMALE\"}"))) SocialSignUpRequestDto req,
		HttpServletRequest httpReq);

	@Operation(summary = "토큰 재발급", description = "Refresh Token이 담긴 쿠키를 통해 Access Token을 재발급합니다.")
	@ApiResponses(value = {@ApiResponse(responseCode = "200", description = "재발급 성공 (SU)"),
		@ApiResponse(responseCode = "401", description = "Invalid refresh token (RF)"),
		@ApiResponse(responseCode = "500", description = "Database error (DBE)")})
	ResponseEntity<ResponseDto<LoginResponseDto>> refreshToken(HttpServletRequest req, HttpServletResponse res);

	@Operation(summary = "비밀번호 변경", description = "로그인 상태에서 비밀번호를 변경합니다.")
	@ApiResponses(value = {@ApiResponse(responseCode = "200", description = "비밀번호 변경 성공 (SU)"),
		@ApiResponse(responseCode = "400", description = "Validation failed (VF)"),
		@ApiResponse(responseCode = "401", description = "Authorization failed (AF)"),
		@ApiResponse(responseCode = "500", description = "Database error (DBE)")})
	ResponseEntity<ResponseDto<Void>> changePw(
		@RequestBody(description = "비밀번호 변경 요청 DTO", required = true, content = @Content(schema = @Schema(implementation = ChangePwRequestDto.class), examples = @ExampleObject(value = "{\"password\":\"newpassword123\"}"))) ChangePwRequestDto changePwRequestDto,
		HttpServletRequest req);

	@Operation(summary = "로그아웃", description = "사용자 인증 쿠키 및 토큰을 삭제하여 로그아웃합니다.")
	@ApiResponses(value = {@ApiResponse(responseCode = "200", description = "로그아웃 성공 (SU)"),
		@ApiResponse(responseCode = "401", description = "Authorization failed (AF)"),
		@ApiResponse(responseCode = "500", description = "Database error (DBE)")})
	ResponseEntity<ResponseDto<Void>> logout(HttpServletRequest req, HttpServletResponse res);

	@Operation(summary = "회원 탈퇴", description = "로그인된 사용자의 계정을 삭제합니다.")
	@ApiResponses(value = {@ApiResponse(responseCode = "200", description = "탈퇴 성공 (SU)"),
		@ApiResponse(responseCode = "401", description = "Authorization failed (AF)"),
		@ApiResponse(responseCode = "500", description = "Database error (DBE)")})
	ResponseEntity<ResponseDto<Void>> withdraw(HttpServletRequest req, HttpServletResponse res);
}