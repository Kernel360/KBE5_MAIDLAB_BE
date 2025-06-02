package kernel.maidlab.api.auth.dto.request;

import kernel.maidlab.common.enums.Gender;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
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
