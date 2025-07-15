package kernel.maidlab.domain.util;

import kernel.maidlab.common.enums.UserType;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class NotificationConnectionKey {
	private final Long userId;
	private final UserType userType;

	public static NotificationConnectionKey of(Long userId, UserType userType) {
		return new NotificationConnectionKey(userId, userType);
	}

	public String toStringKey() {
		return userId + "_" + userType.name();
	}
}