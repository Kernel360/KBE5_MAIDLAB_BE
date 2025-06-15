package kernel.maidlab.api.auth.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginResponseDto {

	private String accessToken;
	private long expirationTime;
	private boolean profileCompleted;

	public LoginResponseDto(String accessToken, long expirationTime) {
		this.accessToken = accessToken;
		this.expirationTime = expirationTime;
		this.profileCompleted = true;
	}
}
