package kernel.maidlab.common.dto.manager.response;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import kernel.maidlab.common.enums.Gender;
import kernel.maidlab.common.enums.Region;
import kernel.maidlab.common.enums.SocialType;
import kernel.maidlab.common.enums.Status;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AdminManagerResponseDto {
	private Long id;
	private String phoneNumber;
	private String name;
	private LocalDate birth;
	private Gender gender;
	private Float averageRate;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
	private String introduceText;
	private String profileImage;
	private SocialType socialType;
 	private List<Region> region;
	private Status isVerified;
	private Boolean isDeleted;
}
