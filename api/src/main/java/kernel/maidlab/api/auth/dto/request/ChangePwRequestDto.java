package kernel.maidlab.api.auth.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ChangePwRequestDto {

	@NotBlank
	@Size(min = 8, max = 20)
	private String password;

}
