package kernel.maidlab.admin.auth.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import kernel.maidlab.admin.auth.dto.request.AdminLoginRequestDto;
import kernel.maidlab.admin.auth.service.AdminAuthService;
import kernel.maidlab.api.auth.dto.response.LoginResponseDto;
import kernel.maidlab.api.util.CookieUtil;
import kernel.maidlab.common.dto.ResponseDto;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/admin/auth")
@RequiredArgsConstructor
public class AdminAuthController {

	private final AdminAuthService adminAuthService;
	private final CookieUtil cookieUtil;

	@PostMapping("/login")
	public ResponseEntity<ResponseDto<LoginResponseDto>> adminLogin(
		@Valid @RequestBody AdminLoginRequestDto adminLoginRequestDto,
		HttpServletResponse res) {
		return adminAuthService.adminLogin(adminLoginRequestDto, res);
	}

	@PostMapping("/refresh")
	public ResponseEntity<ResponseDto<LoginResponseDto>> refreshToken(
		HttpServletRequest req,
		HttpServletResponse res) {
		String refreshToken = cookieUtil.getRefreshTokenFromCookie(req);
		return adminAuthService.refreshToken(refreshToken, res);
	}

	@PostMapping("/logout")
	public ResponseEntity<ResponseDto<Void>> logout(
		HttpServletRequest req,
		HttpServletResponse res) {
		return adminAuthService.logout(req, res);
	}
}
