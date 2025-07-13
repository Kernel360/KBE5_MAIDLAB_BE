package kernel.maidlab.common.enums;

import lombok.Getter;

@Getter
public enum UserType {

	CONSUMER("회원"),
	MANAGER("매니저"),
	ADMIN("관리자");

	private final String name;

	UserType(String name) {
		this.name = name;
	}

}
