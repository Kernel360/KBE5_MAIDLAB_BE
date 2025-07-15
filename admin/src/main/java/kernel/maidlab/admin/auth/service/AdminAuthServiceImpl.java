package kernel.maidlab.admin.auth.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import kernel.maidlab.admin.auth.entity.Admin;
import kernel.maidlab.admin.auth.repository.AdminRepository;
import kernel.maidlab.domain.auth.dto.request.AdminLoginRequestDto;
import kernel.maidlab.domain.auth.dto.response.LoginResponseDto;
import kernel.maidlab.common.dto.ResponseDto;
import kernel.maidlab.common.enums.ResponseType;
import kernel.maidlab.common.util.CookieUtil;
import kernel.maidlab.core.exception.BaseException;
import kernel.maidlab.core.security.AuthenticationHelper;
import kernel.maidlab.core.security.jwt.JwtProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class AdminAuthServiceImpl implements AdminAuthService {

	private final AdminRepository adminRepository;
	private final AdminTokenService adminTokenService;
	private final JwtProperties jwtProperties;
	private final PasswordEncoder passwordEncoder;
	private final CookieUtil cookieUtil;

	// 관리자 로그인
	@Override
	public ResponseEntity<ResponseDto<LoginResponseDto>> adminLogin(AdminLoginRequestDto req, HttpServletResponse res) {
		Admin admin = adminRepository.findByAdminKey(req.getAdminKey())
			.orElseThrow(() -> {
				throw new BaseException(ResponseType.LOGIN_FAILED);
			});

		if (admin.getIsDeleted()) {
			throw new BaseException(ResponseType.ACCOUNT_DELETED);
		}

		if (!passwordEncoder.matches(req.getPassword(), admin.getPassword())) {
			throw new BaseException(ResponseType.LOGIN_FAILED);
		}

		kernel.maidlab.domain.auth.dto.AdminJwtDto.TokenPair tokenPair = adminTokenService.generateAdminTokenPair(admin.getAdminKey());
		long expirationTime = jwtProperties.getExpiration().getAccess();

		cookieUtil.setRefreshTokenCookie(res, tokenPair.getRefreshToken());

		LoginResponseDto responseDto = new LoginResponseDto(
			tokenPair.getAccessToken(),
			expirationTime
		);

		return ResponseDto.success(responseDto);
	}

	// 관리자 토큰 갱신
	@Override
	public ResponseEntity<ResponseDto<LoginResponseDto>> refreshToken(String refreshToken, HttpServletResponse res) {
		kernel.maidlab.domain.auth.dto.AdminJwtDto.AdminRefreshResult result = adminTokenService.refreshAdminTokens(refreshToken);

		if (!result.isSuccess()) {
			throw new BaseException(ResponseType.INVALID_REFRESH_TOKEN);
		}

		cookieUtil.setRefreshTokenCookie(res, result.getRefreshToken());

		long expirationTime = jwtProperties.getExpiration().getAccess();

		LoginResponseDto responseDto = new LoginResponseDto(
			result.getAccessToken(),
			expirationTime
		);

		return ResponseDto.success(responseDto);
	}

	// 관리자 로그아웃
	@Override
	public ResponseEntity<ResponseDto<Void>> logout(HttpServletRequest req, HttpServletResponse res) {
		String adminKey = AuthenticationHelper.getCurrentUserKey();

		adminTokenService.removeAdminRefreshToken(adminKey);
		cookieUtil.clearRefreshTokenCookie(res);

		return ResponseDto.success(null);
	}
}
