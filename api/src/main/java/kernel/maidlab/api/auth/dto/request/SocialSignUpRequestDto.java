package kernel.maidlab.api.auth.dto.request;

import jakarta.validation.constraints.NotBlank;
import kernel.maidlab.common.enums.Gender;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class SocialSignUpRequestDto {

	@NotBlank
	private LocalDate birth;

	@NotBlank
	private Gender gender;

}
