package kernel.maidlab.admin.auth.service;

import org.springframework.http.ResponseEntity;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kernel.maidlab.admin.auth.dto.request.AdminLoginRequestDto;
import kernel.maidlab.api.auth.dto.response.LoginResponseDto;
import kernel.maidlab.common.dto.ResponseDto;

public interface AdminAuthService {
	ResponseEntity<ResponseDto<LoginResponseDto>> adminLogin(AdminLoginRequestDto adminLoginRequestDto,
		HttpServletResponse res);

	ResponseEntity<ResponseDto<LoginResponseDto>> refreshToken(String refreshToken, HttpServletResponse res);

	ResponseEntity<ResponseDto<Void>> logout(HttpServletRequest req, HttpServletResponse res);
}
