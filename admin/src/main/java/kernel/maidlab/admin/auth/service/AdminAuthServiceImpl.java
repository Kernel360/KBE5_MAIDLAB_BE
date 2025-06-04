package kernel.maidlab.admin.auth.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;

import kernel.maidlab.admin.auth.entity.Admin;
import kernel.maidlab.admin.auth.jwt.AdminJwtDto;
import kernel.maidlab.admin.auth.jwt.AdminJwtProvider;
import kernel.maidlab.admin.auth.repository.AdminRepository;
import kernel.maidlab.admin.auth.dto.request.AdminLoginRequestDto;
import kernel.maidlab.api.auth.dto.response.LoginResponseDto;
import kernel.maidlab.api.auth.jwt.JwtProperties;
import kernel.maidlab.api.exception.BaseException;
import kernel.maidlab.api.util.CookieUtil;
import kernel.maidlab.api.util.PasswordUtil;
import kernel.maidlab.common.dto.ResponseDto;
import kernel.maidlab.common.enums.ResponseType;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class AdminAuthServiceImpl implements AdminAuthService {

	private final AdminRepository adminRepository;
	private final AdminJwtProvider adminJwtProvider;
	private final JwtProperties jwtProperties;
	private final PasswordUtil passwordUtil;
	private final CookieUtil cookieUtil;

	// 관리자 로그인
	@Override
	public ResponseEntity<ResponseDto<LoginResponseDto>> adminLogin(AdminLoginRequestDto req, HttpServletResponse res) {
		Admin admin = adminRepository.findByAdminKey(req.getAdminKey())
			.orElseThrow(() -> {
				log.error("Admin을 찾을 수 없음 - AdminKey: {}", req.getAdminKey());
				throw new BaseException(ResponseType.LOGIN_FAILED);
			});

		if (admin.getIsDeleted()) {
			throw new BaseException(ResponseType.ACCOUNT_DELETED);
		}

		if (!passwordUtil.checkPassword(req.getPassword(), admin.getPassword())) {
			throw new BaseException(ResponseType.LOGIN_FAILED);
		}

		AdminJwtDto.TokenPair tokenPair = adminJwtProvider.generateAdminTokenPair(admin.getAdminKey());
		long expirationTime = jwtProperties.getExpiration().getAccess();

		cookieUtil.setRefreshTokenCookie(res, tokenPair.getRefreshToken());

		LoginResponseDto responseDto = new LoginResponseDto(
			tokenPair.getAccessToken(),
			expirationTime
		);

		log.info("관리자 로그인 성공 - AdminKey: {}", admin.getAdminKey());
		return ResponseDto.success(responseDto);
	}

	// 관리자 토큰 갱신
	@Override
	public ResponseEntity<ResponseDto<LoginResponseDto>> refreshToken(String refreshToken, HttpServletResponse res) {
		AdminJwtDto.AdminRefreshResult result = adminJwtProvider.refreshAdminTokens(refreshToken);

		if (!result.isSuccess()) {
			throw new BaseException(ResponseType.INVALID_REFRESH_TOKEN);
		}

		cookieUtil.setRefreshTokenCookie(res, result.getRefreshToken());

		long expirationTime = jwtProperties.getExpiration().getAccess();

		LoginResponseDto responseDto = new LoginResponseDto(
			result.getAccessToken(),
			expirationTime
		);

		log.info("관리자 토큰 갱신 성공");
		return ResponseDto.success(responseDto);
	}

	// 관리자 로그아웃
	@Override
	public ResponseEntity<ResponseDto<Void>> logout(HttpServletRequest req, HttpServletResponse res) {
		String accessToken = adminJwtProvider.extractToken(req);

		if (accessToken == null) {
			throw new BaseException(ResponseType.AUTHORIZATION_FAILED);
		}

		AdminJwtDto.AdminValidationResult validationResult = adminJwtProvider.validateAdminAccessToken(accessToken);

		if (!validationResult.isValid()) {
			throw new BaseException(ResponseType.AUTHORIZATION_FAILED);
		}

		String adminKey = validationResult.getAdminKey();

		adminJwtProvider.removeAdminRefreshToken(adminKey);
		cookieUtil.clearRefreshTokenCookie(res);

		log.info("관리자 로그아웃 성공 - AdminKey: {}", adminKey);
		return ResponseDto.success(null);
	}
}