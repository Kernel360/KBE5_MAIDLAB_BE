package kernel.maidlab.common.dto.manager.request;

import java.time.LocalDate;
import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import kernel.maidlab.common.dto.manager.object.RegionListItem;
import kernel.maidlab.common.dto.manager.object.ScheduleListItem;
import kernel.maidlab.common.dto.manager.object.ServiceListItem;
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

	private LocalDate birth;

	private Gender gender;

	private List<ServiceListItem> serviceTypes;

	@NotNull
	private List<RegionListItem> regions;

	@NotNull
	private List<ScheduleListItem> availableTimes;

	private String introduceText;

}
