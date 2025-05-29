package kernel.maidlab.api.auth.service;

import org.springframework.http.ResponseEntity;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kernel.maidlab.api.auth.dto.request.LoginRequestDto;
import kernel.maidlab.api.auth.dto.request.SignUpRequestDto;
import kernel.maidlab.api.auth.dto.request.SocialLoginRequestDto;
import kernel.maidlab.api.auth.dto.request.SocialSignUpRequestDto;
import kernel.maidlab.api.auth.dto.response.LoginResponseDto;
import kernel.maidlab.api.auth.dto.response.SocialLoginResponseDto;
import kernel.maidlab.common.dto.ResponseDto;

public interface AuthService {

	ResponseEntity<ResponseDto<LoginResponseDto>> login(LoginRequestDto loginRequestDto, HttpServletResponse res);

	ResponseEntity<ResponseDto<SocialLoginResponseDto>> socialLogin(SocialLoginRequestDto socialLoginRequestDto, HttpServletResponse res);

	ResponseEntity<ResponseDto<Void>> signUp(SignUpRequestDto signUpRequestDto);

	ResponseEntity<ResponseDto<Void>> socialSignUp(SocialSignUpRequestDto socialSignUpRequestDto, HttpServletRequest req);

	ResponseEntity<ResponseDto<LoginResponseDto>> refreshToken(String refreshToken, HttpServletResponse res);
}