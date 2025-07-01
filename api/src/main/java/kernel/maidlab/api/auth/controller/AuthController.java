package kernel.maidlab.api.auth.controller;

import kernel.maidlab.api.auth.service.AuthService;
import kernel.maidlab.common.exception.BaseException;
import kernel.maidlab.common.util.CookieUtil;
import kernel.maidlab.common.dto.ResponseDto;
import kernel.maidlab.common.dto.auth.request.ChangePwRequestDto;
import kernel.maidlab.common.dto.auth.request.LoginRequestDto;
import kernel.maidlab.common.dto.auth.request.SignUpRequestDto;
import kernel.maidlab.common.dto.auth.request.SocialLoginRequestDto;
import kernel.maidlab.common.dto.auth.request.SocialSignUpRequestDto;
import kernel.maidlab.common.dto.auth.response.LoginResponseDto;
import kernel.maidlab.common.dto.auth.response.SocialLoginResponseDto;
import kernel.maidlab.common.enums.ResponseType;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController implements AuthApi {

	private final AuthService authService;
	private final CookieUtil cookieUtil;

	@PostMapping("/sign-up")
	public ResponseEntity<ResponseDto<Void>> signUp(@Validated @RequestBody SignUpRequestDto req) {
		log.info("SignUp request received for phoneNumber: {}", req.getPhoneNumber());
		return authService.signUp(req);
	}

	@PostMapping("/login")
	public ResponseEntity<ResponseDto<LoginResponseDto>> login(@Validated @RequestBody LoginRequestDto req,
		HttpServletResponse res) {
		log.info("Login request received for phoneNumber: {}", req.getPhoneNumber());
		return authService.login(req, res);
	}

	@PostMapping("/social-login")
	public ResponseEntity<ResponseDto<SocialLoginResponseDto>> socialLogin(
		@Validated @RequestBody SocialLoginRequestDto req, HttpServletRequest request, HttpServletResponse res) {
		log.info("Social login request received for socialType: {}", req.getSocialType());
		return authService.socialLogin(req, request, res);
	}

	@PostMapping("/social-sign-up")
	public ResponseEntity<ResponseDto<Void>> socialSignUp(@Validated @RequestBody SocialSignUpRequestDto req,
		HttpServletRequest httpReq) {
		log.info("Social sign-up request received for socialType: {}","Google");
		return authService.socialSignUp(req, httpReq);
	}

	@PostMapping("/refresh")
	public ResponseEntity<ResponseDto<LoginResponseDto>> refreshToken(HttpServletRequest req, HttpServletResponse res) {
		log.info("Refresh token request received");
		String refreshToken = cookieUtil.getRefreshTokenFromCookie(req);

		if (refreshToken == null) {
			throw new BaseException(ResponseType.INVALID_REFRESH_TOKEN);
		}

		return authService.refreshToken(refreshToken, res);
	}

	@PatchMapping("/change-password")
	public ResponseEntity<ResponseDto<Void>> changePw(@Validated @RequestBody ChangePwRequestDto changePwRequestDto,
		HttpServletRequest req) {
		log.info("Change password request received");
		return authService.changePw(changePwRequestDto, req);
	}

	@PostMapping("/logout")
	public ResponseEntity<ResponseDto<Void>> logout(HttpServletRequest req, HttpServletResponse res) {
		log.info("Logout request received");
		return authService.logout(req, res);
	}

	@DeleteMapping("/withdraw")
	public ResponseEntity<ResponseDto<Void>> withdraw(HttpServletRequest req, HttpServletResponse res) {
		log.info("Withdraw request received");
		return authService.withdraw(req, res);
	}

}
