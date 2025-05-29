package kernel.maidlab.api.auth.service;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;

import kernel.maidlab.api.auth.client.GoogleResourceApi;
import kernel.maidlab.api.auth.dto.GoogleResourceDto;
import kernel.maidlab.api.auth.dto.GoogleTokenDto;
import kernel.maidlab.api.auth.dto.request.LoginRequestDto;
import kernel.maidlab.api.auth.dto.request.SignUpRequestDto;
import kernel.maidlab.api.auth.dto.request.SocialLoginRequestDto;
import kernel.maidlab.api.auth.dto.request.SocialSignUpRequestDto;
import kernel.maidlab.api.auth.dto.response.LoginResponseDto;
import kernel.maidlab.api.auth.dto.response.SocialLoginResponseDto;
import kernel.maidlab.api.auth.entity.Consumer;
import kernel.maidlab.api.auth.entity.Manager;
import kernel.maidlab.api.auth.jwt.JwtDto;
import kernel.maidlab.api.auth.jwt.JwtProperties;
import kernel.maidlab.api.auth.jwt.JwtProvider;
import kernel.maidlab.api.auth.repository.ConsumerRepository;
import kernel.maidlab.api.auth.repository.ManagerRepository;
import kernel.maidlab.api.exception.BaseException;
import kernel.maidlab.api.util.PasswordUtil;
import kernel.maidlab.api.util.CookieUtil;
import kernel.maidlab.common.dto.ResponseDto;
import kernel.maidlab.common.enums.ResponseType;
import kernel.maidlab.common.enums.SocialType;
import kernel.maidlab.common.enums.UserType;

import lombok.Getter;
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
			consumerRepository.save(consumer);
		} else {
			Manager manager = Manager.createManager(
				req.getPhoneNumber(),
				encodedPassword,
				req.getName(),
				req.getGender(),
				req.getBirth()
			);
			managerRepository.save(manager);
		}

		return ResponseDto.success(null);
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

		if (!passwordUtil.checkPassword(req.getPassword(), consumer.getPassword())) {
			throw new BaseException(ResponseType.LOGIN_FAILED);
		}

		JwtDto.TokenPair tokenPair = jwtProvider.generateTokenPair(consumer.getUuid(), UserType.CONSUMER);
		long expirationTime = jwtProperties.getExpiration().getAccess();

		// 리프레시 토큰을 쿠키에 설정
		cookieUtil.setRefreshTokenCookie(res, tokenPair.getRefreshToken());

		LoginResponseDto responseDto = new LoginResponseDto(
			tokenPair.getAccessToken(),
			expirationTime
		);

		return ResponseDto.success(responseDto);
	}

	private ResponseEntity<ResponseDto<LoginResponseDto>> loginManager(LoginRequestDto req, HttpServletResponse res) {
		Manager manager = managerRepository.findByPhoneNumber(req.getPhoneNumber())
			.orElseThrow(() -> new BaseException(ResponseType.LOGIN_FAILED));

		if (!passwordUtil.checkPassword(req.getPassword(), manager.getPassword())) {
			throw new BaseException(ResponseType.LOGIN_FAILED);
		}

		JwtDto.TokenPair tokenPair = jwtProvider.generateTokenPair(manager.getUuid(), UserType.MANAGER);
		long expirationTime = jwtProperties.getExpiration().getAccess();

		// 리프레시 토큰을 쿠키에 설정
		cookieUtil.setRefreshTokenCookie(res, tokenPair.getRefreshToken());

		LoginResponseDto responseDto = new LoginResponseDto(
			tokenPair.getAccessToken(),
			expirationTime
		);

		return ResponseDto.success(responseDto);
	}

	// 소셜 로그인
	@Override
	public ResponseEntity<ResponseDto<SocialLoginResponseDto>> socialLogin(SocialLoginRequestDto req, HttpServletResponse res) {
		if (req.getCode() == null || req.getCode().trim().isEmpty()) {
			throw new BaseException(ResponseType.VALIDATION_FAILED);
		}

		String accessToken = getGoogleAccessToken(req.getCode());
		GoogleResourceDto googleUser = getGoogleUserResource(accessToken);

		if (req.getUserType() == UserType.CONSUMER) {
			return socialLoginConsumer(googleUser, res);
		} else {
			return socialLoginManager(googleUser, res);
		}
	}

	private String getGoogleAccessToken(String authorizationCode) {
		try {
			GoogleTokenDto tokenDto = googleOAuthService.getGoogleToken(
				authorizationCode,
				googleClientId,
				googleClientSecret,
				googleRedirectUri
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

	private ResponseEntity<ResponseDto<SocialLoginResponseDto>> socialLoginConsumer(GoogleResourceDto googleUser, HttpServletResponse res) {
		Consumer consumer = consumerRepository.findByPhoneNumber(googleUser.getId()).orElse(null);

		if (consumer == null) {
			String tempToken = generateTempToken(googleUser.getId(), googleUser.getName(), UserType.CONSUMER);
			long expirationTime = jwtProperties.getExpiration().getAccess();

			SocialLoginResponseDto responseDto = new SocialLoginResponseDto(
				true,
				tempToken,
				expirationTime
			);

			return ResponseDto.success(responseDto);
		} else {
			JwtDto.TokenPair tokenPair = jwtProvider.generateTokenPair(consumer.getUuid(), UserType.CONSUMER);
			long expirationTime = jwtProperties.getExpiration().getAccess();

			cookieUtil.setRefreshTokenCookie(res, tokenPair.getRefreshToken());

			SocialLoginResponseDto responseDto = new SocialLoginResponseDto(
				false,
				tokenPair.getAccessToken(),
				expirationTime
			);

			return ResponseDto.success(responseDto);
		}
	}

	private ResponseEntity<ResponseDto<SocialLoginResponseDto>> socialLoginManager(GoogleResourceDto googleUser, HttpServletResponse res) {
		Manager manager = managerRepository.findByPhoneNumber(googleUser.getId()).orElse(null);

		if (manager == null) {
			String tempToken = generateTempToken(googleUser.getId(), googleUser.getName(), UserType.MANAGER);
			long expirationTime = jwtProperties.getExpiration().getAccess();

			SocialLoginResponseDto responseDto = new SocialLoginResponseDto(
				true,
				tempToken,
				expirationTime
			);

			return ResponseDto.success(responseDto);
		} else {
			JwtDto.TokenPair tokenPair = jwtProvider.generateTokenPair(manager.getUuid(), UserType.MANAGER);
			long expirationTime = jwtProperties.getExpiration().getAccess();

			cookieUtil.setRefreshTokenCookie(res, tokenPair.getRefreshToken());

			SocialLoginResponseDto responseDto = new SocialLoginResponseDto(
				false,
				tokenPair.getAccessToken(),
				expirationTime
			);

			return ResponseDto.success(responseDto);
		}
	}

	// 회원가입을 위한 임시토큰
	private String generateTempToken(String googleId, String googleName, UserType userType) {
		Date now = new Date();
		Date expiryDate = new Date(now.getTime() + 300000);

		return Jwts.builder()
			.setSubject("temp_social")
			.claim("googleId", googleId)
			.claim("googleName", googleName)
			.claim("userType", userType.name())
			.claim("type", "temp")
			.setIssuedAt(now)
			.setExpiration(expiryDate)
			.signWith(getSignKey(), SignatureAlgorithm.HS512)
			.compact();
	}

	private SecretKey getSignKey() {
		byte[] keyBytes = jwtProperties.getSecretKey().getBytes(StandardCharsets.UTF_8);
		return Keys.hmacShaKeyFor(keyBytes);
	}

	// 구글 정보 추출
	private SocialTempInfo extractGoogleInfo(HttpServletRequest req) {
		String tempToken = jwtProvider.extractToken(req);

		if (tempToken == null) {
			throw new BaseException(ResponseType.INVALID_REFRESH_TOKEN);
		}

		try {
			Claims claims = Jwts.parserBuilder()
				.setSigningKey(getSignKey())
				.build()
				.parseClaimsJws(tempToken)
				.getBody();

			String type = (String) claims.get("type");
			if (!"temp".equals(type)) {
				throw new BaseException(ResponseType.INVALID_REFRESH_TOKEN);
			}

			String googleId = (String) claims.get("googleId");
			String googleName = (String) claims.get("googleName");
			String userTypeStr = (String) claims.get("userType");

			return new SocialTempInfo(googleId, googleName, UserType.valueOf(userTypeStr));

		} catch (Exception e) {
			throw new BaseException(ResponseType.INVALID_REFRESH_TOKEN);
		}
	}

	// 소셜 회원가입
	@Override
	public ResponseEntity<ResponseDto<Void>> socialSignUp(SocialSignUpRequestDto req, HttpServletRequest req2) {
		SocialTempInfo googleInfo = extractGoogleInfo(req2);

		if (req.getUserType() == UserType.CONSUMER) {
			Consumer consumer = Consumer.createSocialConsumer(
				googleInfo.getGoogleId(),
				null,
				req.getName(),
				req.getGender(),
				req.getBirth(),
				SocialType.GOOGLE
			);
			consumerRepository.save(consumer);
		} else {
			Manager manager = Manager.createSocialManager(
				googleInfo.getGoogleId(),
				null,
				req.getName(),
				req.getGender(),
				req.getBirth(),
				SocialType.GOOGLE
			);
			managerRepository.save(manager);
		}

		return ResponseDto.success(null);
	}

	// 내부 클래스
	@Getter
	private static class SocialTempInfo {
		private final String googleId;
		private final String googleName;
		private final UserType userType;

		public SocialTempInfo(String googleId, String googleName, UserType userType) {
			this.googleId = googleId;
			this.googleName = googleName;
			this.userType = userType;
		}
	}

	// 토큰 갱신
	@Override
	public ResponseEntity<ResponseDto<LoginResponseDto>> refreshToken(String refreshToken, HttpServletResponse res) {
		JwtDto.RefreshResult result = jwtProvider.refreshTokens(refreshToken);

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

}
