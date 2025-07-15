package kernel.maidlab.admin.service;


import kernel.maidlab.admin.auth.entity.Admin;
import kernel.maidlab.admin.auth.repository.AdminRepository;
import kernel.maidlab.domain.auth.dto.JwtDto;
import kernel.maidlab.common.enums.UserType;
import kernel.maidlab.core.security.jwt.JwtProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminJwtTokenService {

	private final JwtProvider jwtProvider;
	private final AdminRepository adminRepository;

	// 관리자 토큰 생성
	@Transactional
	public JwtDto.TokenPair generateTokenPair(String adminKey) {
		String accessToken = jwtProvider.generateAccessToken(adminKey, UserType.ADMIN);
		String refreshToken = jwtProvider.generateRefreshToken(adminKey, UserType.ADMIN);

		saveRefreshToken(adminKey, refreshToken);

		return new JwtDto.TokenPair(accessToken, refreshToken);
	}

	// 토큰 갱신
	@Transactional
	public JwtDto.RefreshResult refreshTokens(String refreshToken) {
		if (!jwtProvider.validateToken(refreshToken) || jwtProvider.isNotRefreshToken(refreshToken)) {
			return JwtDto.RefreshResult.failure("유효하지 않은 refresh token");
		}

		String adminKey = jwtProvider.getUserKey(refreshToken);

		String storedRefreshToken = getStoredRefreshToken(adminKey);
		if (storedRefreshToken == null || !storedRefreshToken.equals(refreshToken)) {
			return JwtDto.RefreshResult.failure("저장된 refresh token과 불일치");
		}

		JwtDto.TokenPair tokenPair = generateTokenPair(adminKey);

		return JwtDto.RefreshResult.success(tokenPair.getAccessToken(), tokenPair.getRefreshToken());
	}

	// DB에 refresh 토큰 저장
	@Transactional
	public void saveRefreshToken(String adminKey, String refreshToken) {
		try {
			Admin admin = adminRepository.findByAdminKey(adminKey).orElse(null);
			if (admin != null) {
				admin.updateRefreshToken(refreshToken);
				adminRepository.save(admin);
			}
		} catch (Exception e) {
			log.error("관리자 refresh token 저장 중 오류 - adminKey: {}", adminKey, e);
			throw new RuntimeException("관리자 refresh token 저장 실패", e);
		}
	}

	// DB refresh 토큰 조회
	public String getStoredRefreshToken(String adminKey) {
		try {
			return adminRepository.findByAdminKey(adminKey)
				.map(Admin::getRefreshToken)
				.orElse(null);
		} catch (Exception e) {
			log.error("관리자 저장된 refresh token 조회 중 오류 - adminKey: {}", adminKey, e);
			return null;
		}
	}

	// 로그아웃 시 refresh 토큰 삭제
	@Transactional
	public void removeRefreshToken(String adminKey) {
		saveRefreshToken(adminKey, null);
	}
}