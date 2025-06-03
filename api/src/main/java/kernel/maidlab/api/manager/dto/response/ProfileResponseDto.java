package kernel.maidlab.api.manager.dto.response;

import java.time.LocalDate;
import java.util.List;

import kernel.maidlab.api.manager.dto.object.RegionListItem;
import kernel.maidlab.api.manager.dto.object.ScheduleListItem;
import kernel.maidlab.common.enums.Gender;
import kernel.maidlab.common.enums.ServiceType;
import kernel.maidlab.common.enums.UserType;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ProfileResponseDto {

	private long userid;
	private UserType userType;
	private Boolean isVerified;
	private String profileImage;
	private String name;
	private LocalDate birth;
	private Gender gender;
	private List<RegionListItem> regions;
	private List<ScheduleListItem> schedules;
	private List<ServiceType> services;
	private String introduceText;

}
