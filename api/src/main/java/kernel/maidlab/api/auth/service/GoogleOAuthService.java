package kernel.maidlab.api.auth.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import kernel.maidlab.api.auth.dto.GoogleTokenDto;
import kernel.maidlab.api.exception.BaseException;
import kernel.maidlab.common.enums.ResponseType;
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

		try {
			ResponseEntity<GoogleTokenDto> response = restTemplate.postForEntity(
				url, request, GoogleTokenDto.class);

			return response.getBody();

		} catch (Exception e) {
			throw new BaseException(ResponseType.LOGIN_FAILED);
		}
	}
}