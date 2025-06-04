package kernel.maidlab.common.enums;

import lombok.Getter;

@Getter
public enum Status {

	PENDING("대기"),
	APPROVED("승인"),
	REJECTED("거절"),
	MATCHED("매칭완료"),
	WORKING("작업중"),
	CANCELED("취소"),
	FAILURE("실패"),
	COMPLETED("완료");

	private final String value;

	Status(String value) {
		this.value = value;
	}

}
