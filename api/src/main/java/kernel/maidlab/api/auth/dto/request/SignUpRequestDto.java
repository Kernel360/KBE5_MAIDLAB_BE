package kernel.maidlab.api.auth.dto.request;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import kernel.maidlab.common.enums.Gender;
import kernel.maidlab.common.enums.UserType;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SignUpRequestDto {

	@NotBlank
	private UserType userType;

	@NotBlank
	@Pattern(regexp = "^01[0-9]{8,9}$")
	private String phoneNumber;

	@NotBlank
	@Size(min = 8, max = 20)
	private String password;

	@NotBlank
	private String name;

	@NotBlank
	private LocalDate birth;

	@NotBlank
	private Gender gender;

}
