package kernel.maidlab.api.auth.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kernel.maidlab.api.consumer.repository.ConsumerRepository;
import kernel.maidlab.api.manager.repository.ManagerRepository;
import kernel.maidlab.common.dto.auth.JwtDto;
import kernel.maidlab.common.entity.consumer.Consumer;
import kernel.maidlab.common.entity.manager.Manager;
import kernel.maidlab.common.enums.UserType;
import kernel.maidlab.core.security.jwt.JwtProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class JwtTokenService {

	private final JwtProvider jwtProvider;
	private final ConsumerRepository consumerRepository;
	private final ManagerRepository managerRepository;

	// 토큰 생성
	@Transactional
	public JwtDto.TokenPair generateTokenPair(String userId, UserType userType) {
		String accessToken = jwtProvider.generateAccessToken(userId, userType);
		String refreshToken = jwtProvider.generateRefreshToken(userId, userType);

		saveRefreshToken(userId, userType, refreshToken);

		return new JwtDto.TokenPair(accessToken, refreshToken);
	}

	// 토큰 갱신
	@Transactional
	public JwtDto.RefreshResult refreshTokens(String refreshToken) {
		if (!jwtProvider.validateToken(refreshToken) || jwtProvider.isNotRefreshToken(refreshToken)) {
			return JwtDto.RefreshResult.failure("유효하지 않은 refresh token");
		}

		String userId = jwtProvider.getUserId(refreshToken);
		UserType userType = jwtProvider.getUserType(refreshToken);

		String storedRefreshToken = getStoredRefreshToken(userId, userType);
		if (storedRefreshToken == null || !storedRefreshToken.equals(refreshToken)) {
			return JwtDto.RefreshResult.failure("저장된 refresh token과 불일치");
		}

		JwtDto.TokenPair tokenPair = generateTokenPair(userId, userType);

		return JwtDto.RefreshResult.success(tokenPair.getAccessToken(), tokenPair.getRefreshToken());
	}

	// 사용자 조회
	public Object findUser(String userId, UserType userType) {
		try {
			switch (userType) {
				case CONSUMER:
					return consumerRepository.findByUuid(userId).orElse(null);
				case MANAGER:
					return managerRepository.findByUuid(userId).orElse(null);
				default:
					return null;
			}
		} catch (Exception e) {
			log.error("사용자 조회 중 오류 - userId: {}, userType: {}", userId, userType, e);
			return null;
		}
	}

	// DB에 refresh 토큰 저장
	@Transactional
	public void saveRefreshToken(String userId, UserType userType, String refreshToken) {
		try {
			switch (userType) {
				case CONSUMER:
					Consumer consumer = consumerRepository.findByUuid(userId).orElse(null);
					if (consumer != null) {
						consumer.updateRefreshToken(refreshToken);
						consumerRepository.save(consumer);
					}
					break;
				case MANAGER:
					Manager manager = managerRepository.findByUuid(userId).orElse(null);
					if (manager != null) {
						manager.updateRefreshToken(refreshToken);
						managerRepository.save(manager);
					}
					break;
			}
		} catch (Exception e) {
			log.error("refresh token 저장 중 오류 - userId: {}, userType: {}", userId, userType, e);
			throw new RuntimeException("refresh token 저장 실패", e);
		}
	}

	// DB에 저장된 refresh 토큰 조회
	public String getStoredRefreshToken(String userId, UserType userType) {
		try {
			switch (userType) {
				case CONSUMER:
					return consumerRepository.findByUuid(userId)
						.map(Consumer::getRefreshToken)
						.orElse(null);
				case MANAGER:
					return managerRepository.findByUuid(userId)
						.map(Manager::getRefreshToken)
						.orElse(null);
				default:
					return null;
			}
		} catch (Exception e) {
			log.error("저장된 refresh token 조회 중 오류 - userId: {}, userType: {}", userId, userType, e);
			return null;
		}
	}

	// 로그아웃시 refresh 토큰 삭제
	@Transactional
	public void removeRefreshToken(String userId, UserType userType) {
		saveRefreshToken(userId, userType, null);
	}

	// 로그인 시 사용자 조회 및 토큰생성
	@Transactional
	public JwtDto.TokenPair loginUser(String userId, UserType userType) {
		Object user = findUser(userId, userType);
		if (user == null) {
			return null;
		}

		return generateTokenPair(userId, userType);
	}

	// 토큰 검증 및 사용자 정보 반환
	public JwtDto.ValidationResult validateAccessToken(String token) {
		if (!jwtProvider.validateToken(token) || !jwtProvider.isAccessToken(token)) {
			return JwtDto.ValidationResult.failure("유효하지 않은 access token");
		}

		String userId = jwtProvider.getUserId(token);
		UserType userType = jwtProvider.getUserType(token);

		return JwtDto.ValidationResult.success(userId, userType);
	}

	// 소셜로그인용 임시토큰 생성
	public String generateTempToken(String googleId, String googleName, UserType userType) {
		return jwtProvider.generateTempToken(googleId, googleName, userType);
	}

	// 임시토큰 검증 및 소셜 정보 추출
	public JwtDto.TempTokenInfo validateTempToken(String token) {
		if (!jwtProvider.validateToken(token) || !jwtProvider.isTempToken(token)) {
			return JwtDto.TempTokenInfo.failure("유효하지 않은 임시 토큰");
		}

		try {
			io.jsonwebtoken.Claims claims = jwtProvider.parseClaims(token);
			String googleId = claims.get("googleId", String.class);
			String googleName = claims.get("googleName", String.class);
			String userTypeStr = claims.get("userType", String.class);

			if (googleId == null || googleName == null || userTypeStr == null) {
				return JwtDto.TempTokenInfo.failure("토큰에 필요한 정보가 없습니다.");
			}

			UserType userType = UserType.valueOf(userTypeStr);
			return JwtDto.TempTokenInfo.success(googleId, googleName, userType);

		} catch (Exception e) {
			log.error("임시 토큰 검증 중 오류", e);
			return JwtDto.TempTokenInfo.failure("토큰 검증 실패");
		}
	}

	// 토큰 추출
	public String extractToken(jakarta.servlet.http.HttpServletRequest request) {
		return jwtProvider.extractToken(request);
	}
}