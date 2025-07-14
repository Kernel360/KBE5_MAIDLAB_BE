package kernel.maidlab.common.dto.consumer.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ConsumerProfileRequestDto {
	private String profileImage;
	private String address;
	private String detailAddress;
}
