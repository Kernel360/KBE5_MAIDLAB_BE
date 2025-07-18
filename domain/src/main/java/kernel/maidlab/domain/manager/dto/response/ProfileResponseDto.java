package kernel.maidlab.domain.manager.dto.response;

import kernel.maidlab.common.enums.Gender;
import kernel.maidlab.common.enums.UserType;
import kernel.maidlab.domain.manager.dto.object.RegionListItem;
import kernel.maidlab.domain.manager.dto.object.ScheduleListItem;
import kernel.maidlab.domain.manager.enums.ServiceType;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

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
    private String emergencyCall;

}
