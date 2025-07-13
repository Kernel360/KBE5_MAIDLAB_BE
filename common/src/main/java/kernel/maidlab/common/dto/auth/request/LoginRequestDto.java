package kernel.maidlab.common.dto.auth.request;

import kernel.maidlab.common.enums.UserType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class LoginRequestDto {

	@NotNull
	private UserType userType;

	@NotBlank
	private String phoneNumber;

	@NotBlank
	private String password;

}
