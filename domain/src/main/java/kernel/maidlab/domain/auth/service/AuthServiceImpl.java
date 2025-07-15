package kernel.maidlab.domain.auth.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import kernel.maidlab.domain.auth.dto.JwtDto;
import kernel.maidlab.domain.auth.dto.request.*;
import kernel.maidlab.domain.auth.dto.response.LoginResponseDto;
import kernel.maidlab.domain.auth.dto.response.SocialLoginResponseDto;
import kernel.maidlab.domain.auth.social.GoogleOAuthService;
import kernel.maidlab.domain.auth.social.GoogleResourceApi;
import kernel.maidlab.domain.auth.social.GoogleResourceDto;
import kernel.maidlab.domain.auth.social.GoogleTokenDto;
import kernel.maidlab.domain.consumer.entity.Consumer;
import kernel.maidlab.domain.consumer.repository.ConsumerRepository;
import kernel.maidlab.domain.manager.entity.Manager;
import kernel.maidlab.domain.manager.repository.ManagerRepository;
import kernel.maidlab.domain.util.UserValidator;
import kernel.maidlab.common.dto.ResponseDto;
import kernel.maidlab.common.enums.ResponseType;
import kernel.maidlab.common.enums.SocialType;
import kernel.maidlab.common.enums.UserType;
import kernel.maidlab.common.exception.BaseException;
import kernel.maidlab.common.util.CookieUtil;
import kernel.maidlab.core.security.AuthenticationHelper;
import kernel.maidlab.core.security.jwt.JwtProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class AuthServiceImpl implements AuthService {

	private final ConsumerRepository consumerRepository;
	private final ManagerRepository managerRepository;
	private final JwtTokenService jwtTokenService;
	private final JwtProperties jwtProperties;
	private final PasswordEncoder passwordEncoder;
	private final CookieUtil cookieUtil;
	private final GoogleResourceApi googleResourceApi;
	private final GoogleOAuthService googleOAuthService;
	private final UserValidator userValidator;

	@Value("${oauth2.google.client-id}")
	private String googleClientId;

	@Value("${oauth2.google.client-secret}")
	private String googleClientSecret;

	@Value("${oauth2.google.redirect-uri}")
	private String googleRedirectUri;

	// 휴대폰 회원가입
	@Override
	public ResponseEntity<ResponseDto<Void>> signUp(SignUpRequestDto req) {
		userValidator.validatePhoneNumberDuplication(req.getPhoneNumber(), req.getUserType());

		String encodedPassword = passwordEncoder.encode(req.getPassword());

		if (req.getUserType() == UserType.CONSUMER) {
			Consumer consumer = Consumer.createConsumer(
				req.getPhoneNumber(),
				encodedPassword,
				req.getName(),
				req.getGender(),
				req.getBirth()
			);
			Long id = consumerRepository.save(consumer).getId();
			log.info("Consumer 회원가입 완료 - ID: {}, 이름: {}", id, req.getName());
		} else {
			Manager manager = Manager.createManager(
				req.getPhoneNumber(),
				encodedPassword,
				req.getName(),
				req.getGender(),
				req.getBirth()
			);
			Long id = managerRepository.save(manager).getId();
			log.info("Manager 회원가입 완료 - ID: {}, 이름: {}", id, req.getName());
		}

		return ResponseDto.success();
	}

	// 휴대폰 로그인
	@Override
	public ResponseEntity<ResponseDto<LoginResponseDto>> login(LoginRequestDto req, HttpServletResponse res) {
		Object user = userValidator.validateLoginCredentials(req.getPhoneNumber(), req.getPassword(),
			req.getUserType());

		String userUuid = userValidator.getUserUuid(user);
		JwtDto.TokenPair tokenPair = jwtTokenService.generateTokenPair(userUuid, req.getUserType());
		long expirationTime = jwtProperties.getExpiration().getAccess();

		cookieUtil.setRefreshTokenCookie(res, tokenPair.getRefreshToken());

		boolean profileCompleted = userValidator.hasCompleteProfile(user);
		Long userId = userValidator.getUserId(user);
		String userName = userValidator.getUserName(user);

		log.info("{} 로그인 성공 - ID: {}, 이름: {}, 프로필 완성 여부: {}",
			req.getUserType().getName(), userId, userName, profileCompleted);

		LoginResponseDto responseDto = new LoginResponseDto(
			tokenPair.getAccessToken(),
			expirationTime,
			profileCompleted
		);

		return ResponseDto.success(responseDto);
	}

	// 소셜 로그인
	@Override
	public ResponseEntity<ResponseDto<SocialLoginResponseDto>> socialLogin(
		SocialLoginRequestDto req,
		HttpServletRequest request,
		HttpServletResponse res) {

		if (req.getCode() == null || req.getCode().trim().isEmpty()) {
			log.warn("소셜 로그인 실패 - 인증 코드 누락");
			throw new BaseException(ResponseType.VALIDATION_FAILED);
		}

		String accessToken = getGoogleAccessToken(req.getCode(), request);
		GoogleResourceDto googleUser = getGoogleUserResource(accessToken);
		log.info("Google 사용자 정보 조회 성공 - ID: {}, 이름: {}", googleUser.getId(), googleUser.getName());

		return processSocialLogin(googleUser, req.getUserType(), res);
	}

	private ResponseEntity<ResponseDto<SocialLoginResponseDto>> processSocialLogin(
		GoogleResourceDto googleUser, UserType userType, HttpServletResponse res) {

		Optional<Object> userOpt = userValidator.findBySocialId(googleUser.getId(), userType);

		if (userOpt.isEmpty()) {
			// 신규 사용자 - 임시 토큰 발급
			String tempToken = jwtTokenService.generateTempToken(googleUser.getId(), googleUser.getName(), userType);
			long expirationTime = jwtProperties.getExpiration().getAccess();

			SocialLoginResponseDto responseDto = new SocialLoginResponseDto(
				true, tempToken, expirationTime, false
			);

			return ResponseDto.success(responseDto);
		}

		Object user = userOpt.get();

		// 탈퇴한 계정 체크
		if (userType == UserType.CONSUMER && ((Consumer)user).getIsDeleted()) {
			throw new BaseException(ResponseType.ACCOUNT_DELETED);
		}
		if (userType == UserType.MANAGER && ((Manager)user).getIsDeleted()) {
			throw new BaseException(ResponseType.ACCOUNT_DELETED);
		}

		String userUuid = userValidator.getUserUuid(user);
		boolean profileCompleted = userValidator.hasCompleteProfile(user);

		JwtDto.TokenPair tokenPair = jwtTokenService.generateTokenPair(userUuid, userType);
		long expirationTime = jwtProperties.getExpiration().getAccess();

		cookieUtil.setRefreshTokenCookie(res, tokenPair.getRefreshToken());

		SocialLoginResponseDto responseDto = new SocialLoginResponseDto(
			false, tokenPair.getAccessToken(), expirationTime, profileCompleted
		);

		return ResponseDto.success(responseDto);
	}

	private String getGoogleAccessToken(String authorizationCode, HttpServletRequest request) {
		try {
			String origin = request.getHeader("Origin");
			if (origin == null) {
				origin = request.getHeader("Referer");
				if (origin != null && origin.endsWith("/")) {
					origin = origin.substring(0, origin.length() - 1);
				}
			}

			String dynamicRedirectUri = origin != null ?
				origin + "/google-callback" : googleRedirectUri;

			GoogleTokenDto tokenDto = googleOAuthService.getGoogleToken(
				authorizationCode, googleClientId, googleClientSecret, dynamicRedirectUri
			);

			if (tokenDto == null || tokenDto.getAccessToken() == null) {
				throw new BaseException(ResponseType.LOGIN_FAILED);
			}

			return tokenDto.getAccessToken();
		} catch (BaseException e) {
			throw e;
		} catch (Exception e) {
			throw new BaseException(ResponseType.LOGIN_FAILED);
		}
	}

	private GoogleResourceDto getGoogleUserResource(String accessToken) {
		try {
			GoogleResourceDto resourceDto = googleResourceApi.googleGetResource("Bearer " + accessToken);

			if (resourceDto.getId() == null || resourceDto.getName() == null) {
				throw new BaseException(ResponseType.LOGIN_FAILED);
			}

			return resourceDto;
		} catch (Exception e) {
			throw new BaseException(ResponseType.LOGIN_FAILED);
		}
	}

	// 소셜 회원가입
	@Override
	public ResponseEntity<ResponseDto<Void>> socialSignUp(SocialSignUpRequestDto req, HttpServletRequest req2) {
		JwtDto.TempTokenInfo googleInfo = extractGoogleInfo(req2);

		if (googleInfo.getUserType() == UserType.CONSUMER) {
			Consumer consumer = Consumer.createSocialConsumer(
				googleInfo.getGoogleId(),
				googleInfo.getGoogleName(),
				req.getGender(),
				req.getBirth(),
				SocialType.GOOGLE
			);
			Consumer savedConsumer = consumerRepository.save(consumer);
			log.info("소셜 Consumer 회원가입 완료 - ID: {}, 이름: {}", savedConsumer.getId(), googleInfo.getGoogleName());
		} else {
			Manager manager = Manager.createSocialManager(
				googleInfo.getGoogleId(),
				googleInfo.getGoogleName(),
				req.getGender(),
				req.getBirth(),
				SocialType.GOOGLE
			);
			Manager savedManager = managerRepository.save(manager);
			log.info("소셜 Manager 회원가입 완료 - ID: {}, 이름: {}", savedManager.getId(), googleInfo.getGoogleName());
		}

		return ResponseDto.success(null);
	}

	private JwtDto.TempTokenInfo extractGoogleInfo(HttpServletRequest req) {
		String tempToken = jwtTokenService.extractToken(req);

		if (tempToken == null) {
			throw new BaseException(ResponseType.INVALID_REFRESH_TOKEN);
		}

		JwtDto.TempTokenInfo tempTokenInfo = jwtTokenService.validateTempToken(tempToken);

		if (!tempTokenInfo.isValid()) {
			throw new BaseException(ResponseType.INVALID_REFRESH_TOKEN);
		}

		return tempTokenInfo;
	}

	// 토큰 갱신
	@Override
	public ResponseEntity<ResponseDto<LoginResponseDto>> refreshToken(String refreshToken, HttpServletResponse res) {
		JwtDto.RefreshResult result = jwtTokenService.refreshTokens(refreshToken);

		if (!result.isSuccess()) {
			log.warn("토큰 갱신 실패 - 유효하지 않은 리프레시 토큰");
			throw new BaseException(ResponseType.INVALID_REFRESH_TOKEN);
		}

		cookieUtil.setRefreshTokenCookie(res, result.getRefreshToken());
		log.info("토큰 갱신 성공");

		long expirationTime = jwtProperties.getExpiration().getAccess();

		LoginResponseDto responseDto = new LoginResponseDto(
			result.getAccessToken(),
			expirationTime
		);

		return ResponseDto.success(responseDto);
	}

	// 비밀번호 재설정
	@Override
	public ResponseEntity<ResponseDto<Void>> changePw(ChangePwRequestDto changePwRequestDto, HttpServletRequest req) {
		String uuid = AuthenticationHelper.getCurrentUserId();
		UserType userType = AuthenticationHelper.getCurrentUserType();
		String encodedNewPassword = passwordEncoder.encode(changePwRequestDto.getPassword());

		Object user = userValidator.findByUuid(uuid, userType);
		userValidator.validateSocialAccountPasswordChange(user, userType);

		if (userType == UserType.CONSUMER) {
			Consumer consumer = (Consumer)user;
			consumer.updatePassword(encodedNewPassword);
			consumerRepository.save(consumer);
			log.info("Consumer 비밀번호 변경 완료 - ID: {}", consumer.getId());
		} else {
			Manager manager = (Manager)user;
			manager.updatePassword(encodedNewPassword);
			managerRepository.save(manager);
			log.info("Manager 비밀번호 변경 완료 - ID: {}", manager.getId());
		}

		jwtTokenService.removeRefreshToken(uuid, userType);
		return ResponseDto.success();
	}

	// 로그아웃
	@Override
	public ResponseEntity<ResponseDto<Void>> logout(HttpServletRequest req, HttpServletResponse res) {
		String uuid = AuthenticationHelper.getCurrentUserId();
		UserType userType = AuthenticationHelper.getCurrentUserType();
		jwtTokenService.removeRefreshToken(uuid, userType);
		cookieUtil.clearRefreshTokenCookie(res);

		return ResponseDto.success(null);
	}

	// 회원탈퇴
	@Override
	public ResponseEntity<ResponseDto<Void>> withdraw(HttpServletRequest req, HttpServletResponse res) {
		String uuid = AuthenticationHelper.getCurrentUserId();
		UserType userType = AuthenticationHelper.getCurrentUserType();

		Object user = userValidator.findByUuid(uuid, userType);
		Long userId = userValidator.getUserId(user);

		if (userType == UserType.CONSUMER) {
			Consumer consumer = (Consumer)user;
			consumer.deleteAccount();
			consumerRepository.save(consumer);
			log.info("Consumer 회원탈퇴 완료 - ID: {}", userId);
		} else {
			Manager manager = (Manager)user;
			manager.deleteAccount();
			managerRepository.save(manager);
			log.info("Manager 회원탈퇴 완료 - ID: {}", userId);
		}

		jwtTokenService.removeRefreshToken(uuid, userType);
		cookieUtil.clearRefreshTokenCookie(res);

		return ResponseDto.success(null);
	}
}