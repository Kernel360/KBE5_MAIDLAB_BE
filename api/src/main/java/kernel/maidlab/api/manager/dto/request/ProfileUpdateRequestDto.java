package kernel.maidlab.api.manager.dto.request;

import jakarta.validation.constraints.NotBlank;
import kernel.maidlab.api.manager.dto.object.RegionListItem;
import kernel.maidlab.api.manager.dto.object.ScheduleListItem;
import kernel.maidlab.api.manager.dto.object.ServiceListItem;
import kernel.maidlab.common.enums.Gender;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ProfileUpdateRequestDto {

	private String profileImage;

	@NotBlank
	private String name;

	private LocalDate birth;

	private Gender gender;

	private List<ServiceListItem> serviceTypes;

	@NotBlank
	private List<RegionListItem> regions;

	@NotBlank
	private List<ScheduleListItem> availableTimes;

	private String introduceText;

}
