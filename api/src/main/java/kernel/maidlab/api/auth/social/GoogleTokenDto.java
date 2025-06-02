package kernel.maidlab.api.auth.social;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GoogleTokenDto {

	@JsonProperty("access_token")
	private String accessToken;

	@JsonProperty("refresh_token")
	private String refreshToken;

	@JsonProperty("token_type")
	private String tokenType;

	@JsonProperty("expires_in")
	private Integer expiresIn;

	@JsonProperty("error")
	private String error;

	@JsonProperty("error_description")
	private String errorDescription;

}