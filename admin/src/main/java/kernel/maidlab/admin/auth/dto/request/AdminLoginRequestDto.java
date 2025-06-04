package kernel.maidlab.admin.auth.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AdminLoginRequestDto {

	@NotBlank
	private String adminKey;

	@NotBlank
	private String password;

}
