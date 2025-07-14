package kernel.maidlab.api.manager.dto.response;

import kernel.maidlab.common.entity.manager.Manager;
import kernel.maidlab.common.enums.Gender;
import kernel.maidlab.common.enums.Region;
import kernel.maidlab.common.enums.SocialType;
import kernel.maidlab.common.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
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

	public static AdminManagerResponseDto getInstance(Manager manager) {
		return new AdminManagerResponseDto(
			manager.getId(),
			manager.getPhoneNumber(),
			manager.getName(),
			manager.getBirth(),
			manager.getGender(),
			manager.getAverageRate(),
			manager.getCreatedAt(),
			manager.getUpdatedAt(),
			manager.getIntroduceText(),
			manager.getProfileImage(),
			manager.getSocialType(),
			manager.getRegions(),
			manager.getIsVerified(),
			manager.getIsDeleted()
		);
	}
}
