package kernel.maidlab.domain.consumer.dto.request;

import java.time.LocalDate;

import kernel.maidlab.common.enums.Gender;
import lombok.Getter;

@Getter
public class ConsumerProfileUpdateRequestDto {

	private String profileImage;
	private String name;
	private Gender gender;
	private LocalDate birth;
	private String address;
	private String detailAddress;

}
