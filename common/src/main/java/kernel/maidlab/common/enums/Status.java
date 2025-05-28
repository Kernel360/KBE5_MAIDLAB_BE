package kernel.maidlab.common.enums;

import lombok.Getter;

@Getter
public enum Status {

	PENDING("대기"),
	APPROVED("승인"),
	REJECTED("거절"),
	CANCELED("취소"),
	COMPLETED("완료");

	private final String value;

	Status(String value) {
		this.value = value;
	}

}
