package kernel.maidlab.domain.matching.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AvailableManagerResponseDto {
    private String uuid;
    private String name;
    private Float averageRate;
    private String introduceText;
    private String profileImage;
}
