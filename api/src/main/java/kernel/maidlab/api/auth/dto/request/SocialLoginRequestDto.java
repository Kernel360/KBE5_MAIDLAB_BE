package kernel.maidlab.api.auth.dto.request;

import kernel.maidlab.common.enums.SocialType;
import kernel.maidlab.common.enums.UserType;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SocialLoginRequestDto {

	@NotBlank
	private UserType userType;

	@NotBlank
	private SocialType socialType;

	private String code;

}
