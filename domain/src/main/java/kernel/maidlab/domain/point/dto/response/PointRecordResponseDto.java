package kernel.maidlab.domain.point.dto.response;

import java.time.LocalDateTime;

import kernel.maidlab.common.enums.PointType;
import kernel.maidlab.domain.point.entity.Point;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

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
