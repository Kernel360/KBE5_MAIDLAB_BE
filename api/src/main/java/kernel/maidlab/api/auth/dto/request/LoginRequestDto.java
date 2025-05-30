package kernel.maidlab.api.auth.dto.request;

import kernel.maidlab.common.enums.UserType;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class LoginRequestDto {

	@NotBlank
	private UserType userType;

	@NotBlank
	private String phoneNumber;

	@NotBlank
	private String password;

}
