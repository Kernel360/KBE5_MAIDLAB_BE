package kernel.maidlab.common.enums;

import lombok.Getter;

@Getter
public enum ServiceType {

	GENERAL_CLEANING("일반청소"),
	BABYSITTER("베이비시터"),
	PET_CARE("반려동물 케어");

	private final String value;

	ServiceType(String value) {
		this.value = value;
	}

}
