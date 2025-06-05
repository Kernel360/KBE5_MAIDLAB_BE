package kernel.maidlab.common.enums;

import lombok.Getter;

@Getter
public enum ServiceType {

	HOUSEKEEPING("가사"),
	CARE("돌봄");

	private final String value;

	ServiceType(String value) {
		this.value = value;
	}

}
