package kernel.maidlab.domain.consumer.dto.response;

import java.time.LocalDate;

import kernel.maidlab.common.enums.Gender;
import kernel.maidlab.domain.consumer.entity.Consumer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class ConsumerProfileResponseDto {

	private String profileImage;
	private String name;
	private LocalDate birth;
	private Gender gender;
	private String address;
	private String detailAddress;

	public static ConsumerProfileResponseDto from(Consumer consumer) {

		return new ConsumerProfileResponseDto(
			consumer.getProfileImage(),
			consumer.getName(),
			consumer.getBirth(),
			consumer.getGender(),
			consumer.getAddress(),
			consumer.getDetailAddress()
		);
	}

}
