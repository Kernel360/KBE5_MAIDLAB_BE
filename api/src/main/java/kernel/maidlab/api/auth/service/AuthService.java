package kernel.maidlab.api.auth.service;

import org.springframework.http.ResponseEntity;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kernel.maidlab.common.dto.ResponseDto;
import kernel.maidlab.common.dto.auth.request.ChangePwRequestDto;
import kernel.maidlab.common.dto.auth.request.LoginRequestDto;
import kernel.maidlab.common.dto.auth.request.SignUpRequestDto;
import kernel.maidlab.common.dto.auth.request.SocialLoginRequestDto;
import kernel.maidlab.common.dto.auth.request.SocialSignUpRequestDto;
import kernel.maidlab.common.dto.auth.response.LoginResponseDto;
import kernel.maidlab.common.dto.auth.response.SocialLoginResponseDto;

public interface AuthService {

	ResponseEntity<ResponseDto<LoginResponseDto>> login(LoginRequestDto loginRequestDto, HttpServletResponse res);

	ResponseEntity<ResponseDto<SocialLoginResponseDto>> socialLogin(SocialLoginRequestDto socialLoginRequestDto,
		HttpServletRequest request,
		HttpServletResponse res);

	ResponseEntity<ResponseDto<Void>> signUp(SignUpRequestDto signUpRequestDto);

	ResponseEntity<ResponseDto<Void>> socialSignUp(SocialSignUpRequestDto socialSignUpRequestDto,
		HttpServletRequest req);

	ResponseEntity<ResponseDto<LoginResponseDto>> refreshToken(String refreshToken, HttpServletResponse res);

	ResponseEntity<ResponseDto<Void>> changePw(ChangePwRequestDto changePwRequestDto, HttpServletRequest req);

	ResponseEntity<ResponseDto<Void>> logout(HttpServletRequest req, HttpServletResponse res);

	ResponseEntity<ResponseDto<Void>> withdraw(HttpServletRequest req, HttpServletResponse res);
}
