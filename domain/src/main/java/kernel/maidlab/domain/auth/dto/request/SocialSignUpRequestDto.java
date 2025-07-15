package kernel.maidlab.domain.auth.dto.request;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import kernel.maidlab.common.enums.Gender;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SocialSignUpRequestDto {

	@NotBlank
	private LocalDate birth;

	@NotBlank
	private Gender gender;

}
