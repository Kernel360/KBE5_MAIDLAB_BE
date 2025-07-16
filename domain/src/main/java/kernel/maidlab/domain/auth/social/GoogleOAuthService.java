package kernel.maidlab.domain.auth.social;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import kernel.maidlab.core.aop.enums.LogLevel;
import kernel.maidlab.common.enums.ResponseType;
import kernel.maidlab.core.aop.enums.RetryStrategy;
import kernel.maidlab.core.aop.annotation.exception.ExceptionHandler;
import kernel.maidlab.core.aop.annotation.exception.Retry;
import kernel.maidlab.core.exception.BaseException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class GoogleOAuthService {

	private final RestTemplate restTemplate;

	@Retry(
		maxAttempts = 3,
		delay = 1000,
		strategy = RetryStrategy.EXPONENTIAL,
		retryFor = {Exception.class},
		noRetryFor = {IllegalArgumentException.class}
	)
	@ExceptionHandler(
		value = {HttpClientErrorException.class, RuntimeException.class},
		responseType = ResponseType.LOGIN_FAILED,
		message = "Google OAuth 토큰 발급 중 오류가 발생했습니다",
		logLevel = LogLevel.ERROR,
		enableNotification = true
	)
	public GoogleTokenDto getGoogleToken(String code, String clientId,
		String clientSecret, String redirectUri) {

		String url = "https://oauth2.googleapis.com/token";

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add("code", code);
		params.add("client_id", clientId);
		params.add("client_secret", clientSecret);
		params.add("redirect_uri", redirectUri);
		params.add("grant_type", "authorization_code");

		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);

		try {
			ResponseEntity<GoogleTokenDto> response = restTemplate.postForEntity(
				url, request, GoogleTokenDto.class);

			GoogleTokenDto tokenDto = response.getBody();

			return tokenDto;

		} catch (HttpClientErrorException e) {
			// 4xx 에러 (400, 401, 403 등)
			log.error("Google OAuth API 클라이언트 에러:");
			log.error("  - status: {}", e.getStatusCode());
			log.error("  - status code value: {}", e.getStatusCode().value()); // 추가
			log.error("  - response: '{}'", e.getResponseBodyAsString()); // 따옴표 추가로 빈 값 구분
			log.error("  - response length: {}", e.getResponseBodyAsString().length()); // 추가
			throw new BaseException(ResponseType.LOGIN_FAILED);

		} catch (Exception e) {
			// 기타 에러 (네트워크, 파싱 등)
			log.error("Google OAuth API 예외: {}", e.getMessage(), e);
			throw new BaseException(ResponseType.LOGIN_FAILED);
		}
	}
}
