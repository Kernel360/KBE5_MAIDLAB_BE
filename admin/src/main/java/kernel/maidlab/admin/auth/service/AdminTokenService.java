package kernel.maidlab.admin.auth.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kernel.maidlab.admin.auth.entity.Admin;
import kernel.maidlab.admin.auth.repository.AdminRepository;
import kernel.maidlab.common.dto.auth.AdminJwtDto;
import kernel.maidlab.core.security.jwt.AdminJwtProvider;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminTokenService {

	private static final Logger log = LoggerFactory.getLogger(AdminTokenService.class);

	private final AdminJwtProvider adminJwtProvider;
	private final AdminRepository adminRepository;

	// 관리자 토큰 쌍 생성 및 DB 저장
	@Transactional
	public AdminJwtDto.TokenPair generateAdminTokenPair(String adminKey) {
		String accessToken = adminJwtProvider.generateAdminAccessToken(adminKey);
		String refreshToken = adminJwtProvider.generateAdminRefreshToken(adminKey);

		saveAdminRefreshToken(adminKey, refreshToken);

		return new AdminJwtDto.TokenPair(accessToken, refreshToken);
	}

	// 관리자 토큰 갱신
	@Transactional
	public AdminJwtDto.AdminRefreshResult refreshAdminTokens(String refreshToken) {
		// 토큰 검증
		if (!adminJwtProvider.validateAdminRefreshToken(refreshToken)) {
			return AdminJwtDto.AdminRefreshResult.failure("유효하지 않은 refresh token");
		}

		String adminKey = adminJwtProvider.getAdminKey(refreshToken);

		// DB에 저장된 토큰과 일치하는지 확인
		String storedRefreshToken = getStoredAdminRefreshToken(adminKey);
		if (storedRefreshToken == null || !storedRefreshToken.equals(refreshToken)) {
			return AdminJwtDto.AdminRefreshResult.failure("저장된 refresh token과 불일치");
		}

		// 새 토큰 생성
		AdminJwtDto.TokenPair tokenPair = generateAdminTokenPair(adminKey);

		return AdminJwtDto.AdminRefreshResult.success(tokenPair.getAccessToken(), tokenPair.getRefreshToken());
	}

	// 관리자용 리프레시 토큰 저장
	@Transactional
	public void saveAdminRefreshToken(String adminKey, String refreshToken) {
		try {
			Admin admin = adminRepository.findByAdminKey(adminKey).orElse(null);
			if (admin != null) {
				admin.updateRefreshToken(refreshToken);
				adminRepository.save(admin);
			}
		} catch (Exception e) {
			log.error("관리자 refresh token 저장 중 오류", e);
			throw new RuntimeException("관리자 refresh token 저장 실패", e);
		}
	}

	// 관리자 저장된 리프레시 토큰 조회
	public String getStoredAdminRefreshToken(String adminKey) {
		try {
			return adminRepository.findByAdminKey(adminKey)
				.map(Admin::getRefreshToken)
				.orElse(null);
		} catch (Exception e) {
			log.error("관리자 저장된 refresh token 조회 중 오류", e);
		}
		return null;
	}

	// 관리자 리프레시 토큰 삭제
	@Transactional
	public void removeAdminRefreshToken(String adminKey) {
		saveAdminRefreshToken(adminKey, null);
	}
}