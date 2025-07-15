package kernel.maidlab.admin.auth.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import kernel.maidlab.admin.auth.service.AdminAuthService;
import kernel.maidlab.common.dto.ResponseDto;
import kernel.maidlab.common.util.CookieUtil;
import kernel.maidlab.domain.auth.dto.request.AdminLoginRequestDto;
import kernel.maidlab.domain.auth.dto.response.LoginResponseDto;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/admin/auth")
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
