package kernel.maidlab.api.auth.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SocialLoginResponseDto {

	private boolean isNewUser;
	private String accessToken;
	private long expirationTime;

}
