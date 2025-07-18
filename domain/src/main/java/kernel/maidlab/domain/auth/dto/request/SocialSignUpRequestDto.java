package kernel.maidlab.domain.auth.dto.request;

import jakarta.validation.constraints.NotNull;
import kernel.maidlab.common.enums.Gender;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class SocialSignUpRequestDto {

    @NotNull
    private LocalDate birth;

    @NotNull
    private Gender gender;

    @NotNull
    private String emergencyCall;

}
