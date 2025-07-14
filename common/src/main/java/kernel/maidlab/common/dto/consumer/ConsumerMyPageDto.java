package kernel.maidlab.common.dto.consumer;

import kernel.maidlab.common.entity.consumer.Consumer;
import kernel.maidlab.common.enums.SocialType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ConsumerMyPageDto {
	private String name;
	private int point;
	private String profileImage;
	private SocialType socialType;

	public static ConsumerMyPageDto from(Consumer consumer) {

		return new ConsumerMyPageDto(
			consumer.getName(),
			consumer.getPoint(),
			consumer.getProfileImage(),
			consumer.getSocialType()
		);
	}
}
