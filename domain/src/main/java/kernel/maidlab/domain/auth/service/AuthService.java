package kernel.maidlab.domain.auth.service;

import org.springframework.http.ResponseEntity;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kernel.maidlab.common.dto.ResponseDto;
import kernel.maidlab.domain.auth.dto.request.ChangePwRequestDto;
import kernel.maidlab.domain.auth.dto.request.LoginRequestDto;
import kernel.maidlab.domain.auth.dto.request.SignUpRequestDto;
import kernel.maidlab.domain.auth.dto.request.SocialLoginRequestDto;
import kernel.maidlab.domain.auth.dto.request.SocialSignUpRequestDto;
import kernel.maidlab.domain.auth.dto.response.LoginResponseDto;
import kernel.maidlab.domain.auth.dto.response.SocialLoginResponseDto;

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
