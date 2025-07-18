package kernel.maidlab.auth;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kernel.maidlab.common.dto.ResponseDto;
import kernel.maidlab.common.enums.ResponseType;
import kernel.maidlab.common.util.CookieUtil;
import kernel.maidlab.core.exception.BaseException;
import kernel.maidlab.domain.auth.dto.request.ChangePwRequestDto;
import kernel.maidlab.domain.auth.dto.request.LoginRequestDto;
import kernel.maidlab.domain.auth.dto.request.SignUpRequestDto;
import kernel.maidlab.domain.auth.dto.request.SocialLoginRequestDto;
import kernel.maidlab.domain.auth.dto.request.SocialSignUpRequestDto;
import kernel.maidlab.domain.auth.dto.response.LoginResponseDto;
import kernel.maidlab.domain.auth.dto.response.SocialLoginResponseDto;
import kernel.maidlab.domain.auth.dto.response.SocialSignUpResponseDto;
import kernel.maidlab.domain.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController implements AuthApi {

	private final AuthService authService;
	private final CookieUtil cookieUtil;

	@PostMapping("/sign-up")
	public ResponseEntity<ResponseDto<Void>> signUp(@Validated @RequestBody SignUpRequestDto req) {
		return authService.signUp(req);
	}

	@PostMapping("/login")
	public ResponseEntity<ResponseDto<LoginResponseDto>> login(@Validated @RequestBody LoginRequestDto req,
		HttpServletResponse res) {
		return authService.login(req, res);
	}

	@PostMapping("/social-login")
	public ResponseEntity<ResponseDto<SocialLoginResponseDto>> socialLogin(
		@Validated @RequestBody SocialLoginRequestDto req, HttpServletRequest request, HttpServletResponse res) {
		return authService.socialLogin(req, request, res);
	}

	@PostMapping("/social-sign-up")
	public ResponseEntity<ResponseDto<SocialSignUpResponseDto>> socialSignUp(@Validated @RequestBody SocialSignUpRequestDto req,
		HttpServletRequest httpReq) {
		return authService.socialSignUp(req, httpReq);
	}

	@PostMapping("/refresh")
	public ResponseEntity<ResponseDto<LoginResponseDto>> refreshToken(HttpServletRequest req, HttpServletResponse res) {
		String refreshToken = cookieUtil.getRefreshTokenFromCookie(req);

		if (refreshToken == null) {
			throw new BaseException(ResponseType.INVALID_REFRESH_TOKEN);
		}

		return authService.refreshToken(refreshToken, res);
	}

	@PatchMapping("/change-password")
	public ResponseEntity<ResponseDto<Void>> changePw(@Validated @RequestBody ChangePwRequestDto changePwRequestDto,
		HttpServletRequest req) {
		return authService.changePw(changePwRequestDto, req);
	}

	@PostMapping("/logout")
	public ResponseEntity<ResponseDto<Void>> logout(HttpServletRequest req, HttpServletResponse res) {
		return authService.logout(req, res);
	}

	@DeleteMapping("/withdraw")
	public ResponseEntity<ResponseDto<Void>> withdraw(HttpServletRequest req, HttpServletResponse res) {
		return authService.withdraw(req, res);
	}

}
