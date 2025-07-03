package kernel.maidlab.common.dto.point.response;

import kernel.maidlab.common.entity.point.Point;
import kernel.maidlab.common.enums.PointType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class PointRecordResponseDto {

    private Integer amount;
    private PointType pointType;
    private String description;
    private LocalDateTime createdAt;

    public static PointRecordResponseDto from(Point point) {
        return PointRecordResponseDto.builder()
                .amount(point.getAmount())
                .pointType(point.getPointType())
                .description(point.getDescription())
                .createdAt(point.getCreatedAt())
                .build();
    }


}
