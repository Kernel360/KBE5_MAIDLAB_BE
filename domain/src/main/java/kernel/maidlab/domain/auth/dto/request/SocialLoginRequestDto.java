package kernel.maidlab.domain.auth.dto.request;

import jakarta.validation.constraints.NotNull;
import kernel.maidlab.common.enums.SocialType;
import kernel.maidlab.common.enums.UserType;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SocialLoginRequestDto {

	@NotNull
	private UserType userType;

	@NotNull
	private SocialType socialType;

	private String code;

}
