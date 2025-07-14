package kernel.maidlab.common.dto.consumer.response;

import java.util.List;

import kernel.maidlab.common.entity.manager.Manager;
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
	private float averageRate;
	private String introduceText;

	public static List<BlackListedManagerResponseDto> from(List<Manager> BlacklistedManagerList) {
		return BlacklistedManagerList.stream()
			.map(m -> new BlackListedManagerResponseDto(
				m.getUuid(),
				m.getName(),
				m.getProfileImage(),
				m.getAverageRate(),
				m.getIntroduceText()
			))
			.toList();
	}

}
