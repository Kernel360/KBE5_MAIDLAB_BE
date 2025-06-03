package kernel.maidlab.api.manager.dto.request;

import java.time.LocalDate;
import java.util.List;

import jakarta.validation.constraints.NotBlank;
import kernel.maidlab.api.manager.dto.object.RegionListItem;
import kernel.maidlab.api.manager.dto.object.ScheduleListItem;
import kernel.maidlab.api.manager.dto.object.ServiceListItem;
import kernel.maidlab.common.enums.Gender;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ProfileUpdateRequestDto {

	private String profileImage;

	@NotBlank
	private String name;

	@NotBlank
	private LocalDate birth;

	@NotBlank
	private Gender gender;

	@NotBlank
	private List<ServiceListItem> serviceTypes;

	@NotBlank
	private List<RegionListItem> regions;

	@NotBlank
	private List<ScheduleListItem> availableTimes;

	private String introduceText;

}
