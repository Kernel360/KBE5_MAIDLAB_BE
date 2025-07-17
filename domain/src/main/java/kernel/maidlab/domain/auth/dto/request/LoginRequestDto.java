package kernel.maidlab.domain.auth.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import kernel.maidlab.common.enums.UserType;
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
