package kernel.maidlab.domain.manager.dto.request;

import java.time.LocalDate;
import java.util.List;

import jakarta.validation.constraints.NotBlank;
import kernel.maidlab.common.enums.Gender;
import kernel.maidlab.domain.manager.dto.object.RegionListItem;
import kernel.maidlab.domain.manager.dto.object.ScheduleListItem;
import kernel.maidlab.domain.manager.dto.object.ServiceListItem;
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

	private LocalDate birth;

	private Gender gender;

	private List<ServiceListItem> serviceTypes;

	@NotBlank
	private List<RegionListItem> regions;

	@NotBlank
	private List<ScheduleListItem> availableTimes;

	private String introduceText;

}
