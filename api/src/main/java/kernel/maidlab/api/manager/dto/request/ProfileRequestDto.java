package kernel.maidlab.api.manager.dto.request;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
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

	@NotEmpty
	@Valid
	private List<ServiceListItem> serviceTypes;

	@NotEmpty
	@Valid
	private List<RegionListItem> regions;

	@NotEmpty
	@Valid
	private List<ScheduleListItem> availableTimes;

	private String introduceText;

	@NotEmpty
	@Valid
	private List<DocumentListItem> documents;

}
