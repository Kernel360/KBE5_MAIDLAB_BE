package kernel.maidlab.api.manager.dto;

import java.time.LocalDate;
import java.util.List;

import kernel.maidlab.common.enums.Gender;
import kernel.maidlab.common.enums.Region;
import kernel.maidlab.common.enums.Status;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ManagerResponseDto {
	private String uuid;
	private String phoneNumber;
	private String name;
	private LocalDate birth;
	private Gender gender;
	private Float averageRate;
	private List<Region> region;
	private Status isVerified;
	private Boolean isDeleted;
}
