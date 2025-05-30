package kernel.maidlab.api.consumer.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class BlackListedManagerResponseDto {

    private String managerUuid;
    private String name;
    private String profileImage;
    private float  averageRate;
    private String introduceText;

}
