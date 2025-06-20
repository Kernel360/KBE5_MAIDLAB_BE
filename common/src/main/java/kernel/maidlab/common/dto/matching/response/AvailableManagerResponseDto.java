package kernel.maidlab.common.dto.matching.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AvailableManagerResponseDto {
	private String uuid;
	private String name;
	private Float averageRate;
	private String introduceText;
	private String profileImage;
}
