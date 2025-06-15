// SocialLoginResponseDto.java - 대안적 수정
package kernel.maidlab.api.auth.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SocialLoginResponseDto {

	private boolean isNewUser;
	private String accessToken;
	private long expirationTime;
	private boolean profileCompleted;

	public SocialLoginResponseDto(boolean isNewUser, String accessToken, long expirationTime) {
		this.isNewUser = isNewUser;
		this.accessToken = accessToken;
		this.expirationTime = expirationTime;
		this.profileCompleted = false;
	}

	public static SocialLoginResponseDto newUser(String tempToken, long expirationTime) {
		return new SocialLoginResponseDto(true, tempToken, expirationTime, false);
	}

	public static SocialLoginResponseDto existingUserWithoutProfile(String accessToken, long expirationTime) {
		return new SocialLoginResponseDto(false, accessToken, expirationTime, false);
	}

	public static SocialLoginResponseDto existingUserWithProfile(String accessToken, long expirationTime) {
		return new SocialLoginResponseDto(false, accessToken, expirationTime, true);
	}
}