package kernel.maidlab.common.dto.manager.request;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
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
