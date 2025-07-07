package kernel.maidlab.api.auth.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;

import kernel.maidlab.api.auth.social.*;
import kernel.maidlab.common.dto.auth.JwtDto;
import kernel.maidlab.common.dto.auth.request.ChangePwRequestDto;
import kernel.maidlab.common.dto.auth.request.LoginRequestDto;
import kernel.maidlab.common.dto.auth.request.SignUpRequestDto;
import kernel.maidlab.common.dto.auth.request.SocialLoginRequestDto;
import kernel.maidlab.common.dto.auth.request.SocialSignUpRequestDto;
import kernel.maidlab.common.dto.auth.response.LoginResponseDto;
import kernel.maidlab.common.dto.auth.response.SocialLoginResponseDto;
import kernel.maidlab.common.entity.consumer.Consumer;
import kernel.maidlab.common.entity.manager.Manager;
import kernel.maidlab.api.auth.jwt.*;
import kernel.maidlab.api.consumer.repository.ConsumerRepository;
import kernel.maidlab.api.manager.repository.ManagerRepository;
import kernel.maidlab.common.exception.BaseException;
import kernel.maidlab.api.util.PasswordUtil;
import kernel.maidlab.common.util.CookieUtil;
import kernel.maidlab.common.dto.ResponseDto;
import kernel.maidlab.common.enums.ResponseType;
import kernel.maidlab.common.enums.SocialType;
import kernel.maidlab.common.enums.UserType;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class AuthServiceImpl implements AuthService {

	private final ConsumerRepository consumerRepository;
	private final ManagerRepository managerRepository;
	private final JwtProvider jwtProvider;
	private final JwtProperties jwtProperties;
	private final PasswordUtil passwordUtil;
	private final CookieUtil cookieUtil;
	private final GoogleResourceApi googleResourceApi;
	private final GoogleOAuthService googleOAuthService;

	@Value("${oauth2.google.client-id}")
	private String googleClientId;

	@Value("${oauth2.google.client-secret}")
	private String googleClientSecret;

	@Value("${oauth2.google.redirect-uri}")
	private String googleRedirectUri;

	// 휴대폰 중복검사
	private void phoneNumberDuplication(String phoneNumber, UserType userType) {
		if (userType == UserType.CONSUMER) {
			if (consumerRepository.findByPhoneNumber(phoneNumber).isPresent()) {
				log.warn("Consumer 휴대폰 번호 중복: {}", phoneNumber);
				throw new BaseException(ResponseType.DUPLICATE_TEL_NUMBER);
			}
		} else {
			if (managerRepository.findByPhoneNumber(phoneNumber).isPresent()) {
				log.warn("Manager 휴대폰 번호 중복: {}", phoneNumber);
				throw new BaseException(ResponseType.DUPLICATE_TEL_NUMBER);
			}
		}
	}

	// 휴대폰 회원가입
	@Override
	public ResponseEntity<ResponseDto<Void>> signUp(SignUpRequestDto req) {
		phoneNumberDuplication(req.getPhoneNumber(), req.getUserType());

		String encodedPassword = passwordUtil.encryptPassword(req.getPassword());

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
		if (req.getUserType() == UserType.CONSUMER) {
			return loginConsumer(req, res);
		} else {
			return loginManager(req, res);
		}
	}

	private ResponseEntity<ResponseDto<LoginResponseDto>> loginConsumer(LoginRequestDto req, HttpServletResponse res) {
		Consumer consumer = consumerRepository.findByPhoneNumber(req.getPhoneNumber())
			.orElseThrow(() -> new BaseException(ResponseType.LOGIN_FAILED));

		if (consumer.getIsDeleted()) {
			log.warn("탈퇴한 Consumer 계정 로그인 시도 - ID: {}", consumer.getId());
			throw new BaseException(ResponseType.ACCOUNT_DELETED);
		}

		if (!passwordUtil.checkPassword(req.getPassword(), consumer.getPassword())) {
			log.warn("Consumer 로그인 실패 - 잘못된 비밀번호, ID: {}", consumer.getId());
			throw new BaseException(ResponseType.LOGIN_FAILED);
		}

		JwtDto.TokenPair tokenPair = jwtProvider.generateTokenPair(consumer.getUuid(), UserType.CONSUMER);
		long expirationTime = jwtProperties.getExpiration().getAccess();

		cookieUtil.setRefreshTokenCookie(res, tokenPair.getRefreshToken());

		boolean profileCompleted = consumer.hasCompleteProfile();
		log.info("Consumer 로그인 성공 - ID: {},이름: {}, 프로필 완성 여부: {},", consumer.getId(), consumer.getName(),profileCompleted);

		LoginResponseDto responseDto = new LoginResponseDto(
			tokenPair.getAccessToken(),
			expirationTime,
			profileCompleted
		);

		return ResponseDto.success(responseDto);
	}

	private ResponseEntity<ResponseDto<LoginResponseDto>> loginManager(LoginRequestDto req, HttpServletResponse res) {
		Manager manager = managerRepository.findByPhoneNumber(req.getPhoneNumber())
			.orElseThrow(() -> new BaseException(ResponseType.LOGIN_FAILED));

		if (manager.getIsDeleted()) {
			log.warn("탈퇴한 Manager 계정 로그인 시도 - ID: {}", manager.getId());
			throw new BaseException(ResponseType.ACCOUNT_DELETED);
		}

		if (!passwordUtil.checkPassword(req.getPassword(), manager.getPassword())) {
			log.warn("Manager 로그인 실패 - 잘못된 비밀번호, ID: {}", manager.getId());
			throw new BaseException(ResponseType.LOGIN_FAILED);
		}

		JwtDto.TokenPair tokenPair = jwtProvider.generateTokenPair(manager.getUuid(), UserType.MANAGER);
		long expirationTime = jwtProperties.getExpiration().getAccess();

		cookieUtil.setRefreshTokenCookie(res, tokenPair.getRefreshToken());

		boolean profileCompleted = manager.hasCompleteProfile();
		log.info("Manager 로그인 성공 - ID: {}, 프로필 완성 여부: {}", manager.getId(), profileCompleted);

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

		String accessToken = getGoogleAccessToken(req.getCode(), request);  // request 전달
		GoogleResourceDto googleUser = getGoogleUserResource(accessToken);
		log.info("Google 사용자 정보 조회 성공 - ID: {}, 이름: {}", googleUser.getId(), googleUser.getName());

		if (req.getUserType() == UserType.CONSUMER) {
			return socialLoginConsumer(googleUser, res);
		} else {
			return socialLoginManager(googleUser, res);
		}
	}

	private String getGoogleAccessToken(String authorizationCode, HttpServletRequest request) {
		try {
			// 요청 헤더에서 origin 추출
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
				authorizationCode,
				googleClientId,
				googleClientSecret,
				dynamicRedirectUri
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

	private ResponseEntity<ResponseDto<SocialLoginResponseDto>> socialLoginConsumer(GoogleResourceDto googleUser,
		HttpServletResponse res) {
		Consumer consumer = consumerRepository.findByPhoneNumber(googleUser.getId()).orElse(null);

		if (consumer == null) {
			String tempToken = jwtProvider.generateTempToken(googleUser.getId(), googleUser.getName(),
				UserType.CONSUMER);
			long expirationTime = jwtProperties.getExpiration().getAccess();

			SocialLoginResponseDto responseDto = new SocialLoginResponseDto(
				true,
				tempToken,
				expirationTime,
				false
			);

			return ResponseDto.success(responseDto);
		} else {
			if (consumer.getIsDeleted()) {
				throw new BaseException(ResponseType.ACCOUNT_DELETED);
			}

			if (!consumer.hasCompleteProfile()) {
				JwtDto.TokenPair tokenPair = jwtProvider.generateTokenPair(consumer.getUuid(), UserType.CONSUMER);
				long expirationTime = jwtProperties.getExpiration().getAccess();

				cookieUtil.setRefreshTokenCookie(res, tokenPair.getRefreshToken());

				SocialLoginResponseDto responseDto = new SocialLoginResponseDto(
					false,
					tokenPair.getAccessToken(),
					expirationTime,
					false
				);

				return ResponseDto.success(responseDto);
			}

			JwtDto.TokenPair tokenPair = jwtProvider.generateTokenPair(consumer.getUuid(), UserType.CONSUMER);
			long expirationTime = jwtProperties.getExpiration().getAccess();

			cookieUtil.setRefreshTokenCookie(res, tokenPair.getRefreshToken());

			SocialLoginResponseDto responseDto = new SocialLoginResponseDto(
				false,
				tokenPair.getAccessToken(),
				expirationTime,
				true
			);

			return ResponseDto.success(responseDto);
		}
	}

	private ResponseEntity<ResponseDto<SocialLoginResponseDto>> socialLoginManager(GoogleResourceDto googleUser,
		HttpServletResponse res) {
		Manager manager = managerRepository.findByPhoneNumber(googleUser.getId()).orElse(null);

		if (manager == null) {
			String tempToken = jwtProvider.generateTempToken(googleUser.getId(), googleUser.getName(),
				UserType.MANAGER);
			long expirationTime = jwtProperties.getExpiration().getAccess();

			SocialLoginResponseDto responseDto = new SocialLoginResponseDto(
				true,
				tempToken,
				expirationTime,
				false
			);

			return ResponseDto.success(responseDto);
		} else {
			if (manager.getIsDeleted()) {
				throw new BaseException(ResponseType.ACCOUNT_DELETED);
			}

			if (!manager.hasCompleteProfile()) {
				JwtDto.TokenPair tokenPair = jwtProvider.generateTokenPair(manager.getUuid(), UserType.MANAGER);
				long expirationTime = jwtProperties.getExpiration().getAccess();

				cookieUtil.setRefreshTokenCookie(res, tokenPair.getRefreshToken());

				SocialLoginResponseDto responseDto = new SocialLoginResponseDto(
					false,
					tokenPair.getAccessToken(),
					expirationTime,
					false
				);

				return ResponseDto.success(responseDto);
			}

			JwtDto.TokenPair tokenPair = jwtProvider.generateTokenPair(manager.getUuid(), UserType.MANAGER);
			long expirationTime = jwtProperties.getExpiration().getAccess();

			cookieUtil.setRefreshTokenCookie(res, tokenPair.getRefreshToken());

			SocialLoginResponseDto responseDto = new SocialLoginResponseDto(
				false,
				tokenPair.getAccessToken(),
				expirationTime,
				true
			);

			return ResponseDto.success(responseDto);
		}
	}

	// 소셜 회원가입
	private JwtDto.TempTokenInfo extractGoogleInfo(HttpServletRequest req) {
		String tempToken = jwtProvider.extractToken(req);

		if (tempToken == null) {
			throw new BaseException(ResponseType.INVALID_REFRESH_TOKEN);
		}

		JwtDto.TempTokenInfo tempTokenInfo = jwtProvider.validateTempToken(tempToken);

		if (!tempTokenInfo.isValid()) {
			throw new BaseException(ResponseType.INVALID_REFRESH_TOKEN);
		}

		return tempTokenInfo;
	}

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

	// 토큰 갱신
	@Override
	public ResponseEntity<ResponseDto<LoginResponseDto>> refreshToken(String refreshToken, HttpServletResponse res) {
		JwtDto.RefreshResult result = jwtProvider.refreshTokens(refreshToken);

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
		String uuid = (String)req.getAttribute(JwtFilter.CURRENT_USER_UUID_KEY);
		UserType userType = (UserType)req.getAttribute(JwtFilter.CURRENT_USER_TYPE_KEY);
		String encodedNewPassword = passwordUtil.encryptPassword(changePwRequestDto.getPassword());

		if (userType == UserType.CONSUMER) {
			Consumer consumer = consumerRepository.findByUuid(uuid)
				.orElseThrow(() -> new BaseException(ResponseType.AUTHORIZATION_FAILED));

			if (consumer.getSocialType() != null) {
				log.warn("소셜 계정 비밀번호 변경 시도 - ID: {}, 소셜 타입: {}", consumer.getId(), consumer.getSocialType());
				throw new BaseException(ResponseType.VALIDATION_FAILED);
			}

			consumer.updatePassword(encodedNewPassword);
			consumerRepository.save(consumer);
			log.info("Consumer 비밀번호 변경 완료 - ID: {}", consumer.getId());
		} else {
			Manager manager = managerRepository.findByUuid(uuid)
				.orElseThrow(() -> new BaseException(ResponseType.AUTHORIZATION_FAILED));

			if (manager.getSocialType() != null) {
				log.warn("소셜 계정 비밀번호 변경 시도 - ID: {}, 소셜 타입: {}", manager.getId(), manager.getSocialType());
				throw new BaseException(ResponseType.VALIDATION_FAILED);
			}

			manager.updatePassword(encodedNewPassword);
			managerRepository.save(manager);
			log.info("Manager 비밀번호 변경 완료 - ID: {}", manager.getId());
		}

		jwtProvider.removeRefreshToken(uuid, userType);

		return ResponseDto.success();
	}

	// 로그아웃
	@Override
	public ResponseEntity<ResponseDto<Void>> logout(HttpServletRequest req, HttpServletResponse res) {
		String uuid = (String)req.getAttribute(JwtFilter.CURRENT_USER_UUID_KEY);
		UserType userType = (UserType)req.getAttribute(JwtFilter.CURRENT_USER_TYPE_KEY);
		jwtProvider.removeRefreshToken(uuid, userType);
		cookieUtil.clearRefreshTokenCookie(res);

		return ResponseDto.success(null);
	}

	// 회원탈퇴
	@Override
	public ResponseEntity<ResponseDto<Void>> withdraw(HttpServletRequest req, HttpServletResponse res) {
		String uuid = (String)req.getAttribute(JwtFilter.CURRENT_USER_UUID_KEY);
		UserType userType = (UserType)req.getAttribute(JwtFilter.CURRENT_USER_TYPE_KEY);
		if (userType == UserType.CONSUMER) {
			Consumer consumer = consumerRepository.findByUuid(uuid)
				.orElseThrow(() -> new BaseException(ResponseType.AUTHORIZATION_FAILED));

			consumer.deleteAccount();
			consumerRepository.save(consumer);
			log.info("Consumer 회원탈퇴 완료 - ID: {}", consumer.getId());

		} else {
			Manager manager = managerRepository.findByUuid(uuid)
				.orElseThrow(() -> new BaseException(ResponseType.AUTHORIZATION_FAILED));

			manager.deleteAccount();
			managerRepository.save(manager);
			log.info("Manager 회원탈퇴 완료 - ID: {}", manager.getId());

		}

		jwtProvider.removeRefreshToken(uuid, userType);
		cookieUtil.clearRefreshTokenCookie(res);

		return ResponseDto.success(null);
	}
}
