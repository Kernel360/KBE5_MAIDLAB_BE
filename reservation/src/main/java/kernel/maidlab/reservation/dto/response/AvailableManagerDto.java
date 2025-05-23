package kernel.maidlab.reservation.dto.response;

import java.util.List;

import kernel.maidlab.reservation.enums.AdditionalService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AvailableManagerDto {
	private Long managerId;
	private String managerName;
	private String managerProfileImageUrl;
	private Integer managerRating;
	private String introduceText;
	// private List<AdditionalService> additionalServiceList;
}
