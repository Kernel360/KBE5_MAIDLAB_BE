package kernel.maidlab.api.manager.dto.response;

import kernel.maidlab.common.enums.UserType;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MypageResponseDto {

	private long userid;
	private UserType userType;
	private String profileImage;
	private String name;
	private Boolean isVerified;

}
