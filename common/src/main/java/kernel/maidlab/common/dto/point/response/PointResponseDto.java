package kernel.maidlab.common.dto.point.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PointResponseDto {
    private Long totalPoint;

    public static PointResponseDto from (Long totalPoint){
        return new PointResponseDto(totalPoint);
    }
}
