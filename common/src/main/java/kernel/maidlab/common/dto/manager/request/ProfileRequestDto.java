package kernel.maidlab.common.dto.manager.request;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import kernel.maidlab.common.dto.manager.object.DocumentListItem;
import kernel.maidlab.common.dto.manager.object.RegionListItem;
import kernel.maidlab.common.dto.manager.object.ScheduleListItem;
import kernel.maidlab.common.dto.manager.object.ServiceListItem;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ProfileRequestDto {

	private String profileImage;

	@NotBlank
	private List<ServiceListItem> serviceTypes;

	@NotBlank
	private List<RegionListItem> regions;

	@NotBlank
	private List<ScheduleListItem> availableTimes;

	private String introduceText;

	@NotBlank
	private List<DocumentListItem> documents;

}
