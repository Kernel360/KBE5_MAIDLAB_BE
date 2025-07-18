package kernel.maidlab.domain.auth.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SocialSignUpResponseDto {

    private String accessToken;
    private LocalDateTime expirationTime;
    private boolean profileCompleted;

}