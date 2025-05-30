package kernel.maidlab.api.consumer.dto.response;

import kernel.maidlab.common.enums.Region;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class LikedManagerResponseDto {

    private String managerUuid;
    private String name;
    private String profileImage;
    private float  averageRate;
    private String introduceText;
    private List<Region> region;

}
