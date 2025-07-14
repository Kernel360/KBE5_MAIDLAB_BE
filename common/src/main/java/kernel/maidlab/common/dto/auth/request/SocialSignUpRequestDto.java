package kernel.maidlab.common.dto.auth.request;

import java.time.LocalDate;

import jakarta.validation.constraints.NotNull;
import kernel.maidlab.common.enums.Gender;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SocialSignUpRequestDto {

	@NotNull
	private LocalDate birth;

	@NotNull
	private Gender gender;

}
