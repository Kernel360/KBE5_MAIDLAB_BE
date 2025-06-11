package kernel.maidlab.api.auth.social;

import kernel.maidlab.api.exception.BaseException;
import kernel.maidlab.common.enums.ResponseType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class GoogleOAuthService {

	@Autowired
	private RestTemplate restTemplate;

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

		log.info("Google OAuth API 호출:");
		log.info("  - URL: {}", url);
		log.info("  - client_id: {}", clientId != null ? clientId.substring(0, 10) + "..." : "null");
		log.info("  - redirect_uri: {}", redirectUri);
		log.info("  - code: {}", code != null ? code.substring(0, 10) + "..." : "null");
		log.info("  - grant_type: authorization_code");

		try {
			ResponseEntity<GoogleTokenDto> response = restTemplate.postForEntity(
				url, request, GoogleTokenDto.class);

			log.info("Google OAuth API 성공:");
			log.info("  - status: {}", response.getStatusCode());
			log.info("  - body: {}", response.getBody() != null ? "Present" : "null");

			GoogleTokenDto tokenDto = response.getBody();
			if (tokenDto != null) {
				log.info("  - access_token: {}", tokenDto.getAccessToken() != null ? "Present" : "null");
				log.info("  - token_type: {}", tokenDto.getTokenType());
			}

			return tokenDto;

		} catch (HttpClientErrorException e) {
			// 4xx 에러 (400, 401, 403 등)
			log.error("Google OAuth API 클라이언트 에러:");
			log.error("  - status: {}", e.getStatusCode());
			log.error("  - response: {}", e.getResponseBodyAsString());
			throw new BaseException(ResponseType.LOGIN_FAILED);

		} catch (Exception e) {
			// 기타 에러 (네트워크, 파싱 등)
			log.error("Google OAuth API 예외: {}", e.getMessage(), e);
			throw new BaseException(ResponseType.LOGIN_FAILED);
		}
	}
}