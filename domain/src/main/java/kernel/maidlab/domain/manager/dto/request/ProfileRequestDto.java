package kernel.maidlab.domain.manager.dto.request;

import jakarta.validation.constraints.NotBlank;
import kernel.maidlab.domain.manager.dto.object.DocumentListItem;
import kernel.maidlab.domain.manager.dto.object.RegionListItem;
import kernel.maidlab.domain.manager.dto.object.ScheduleListItem;
import kernel.maidlab.domain.manager.dto.object.ServiceListItem;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

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
