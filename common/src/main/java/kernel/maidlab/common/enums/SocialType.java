package kernel.maidlab.common.enums;

import lombok.Getter;

@Getter
public enum SocialType {

	KAKAO("카카오"),
	GOOGLE("구글");

	private final String value;

	SocialType(String value) {
		this.value = value;
	}

}
