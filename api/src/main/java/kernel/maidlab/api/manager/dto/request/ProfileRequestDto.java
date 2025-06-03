package kernel.maidlab.api.manager.dto.request;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import kernel.maidlab.api.manager.dto.object.DocumentListItem;
import kernel.maidlab.api.manager.dto.object.RegionListItem;
import kernel.maidlab.api.manager.dto.object.ScheduleListItem;
import kernel.maidlab.api.manager.dto.object.ServiceListItem;
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
