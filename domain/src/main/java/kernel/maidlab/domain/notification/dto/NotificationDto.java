package kernel.maidlab.domain.notification.dto;

import java.time.LocalDateTime;

import kernel.maidlab.common.enums.NotificationType;
import kernel.maidlab.common.enums.UserType;
import kernel.maidlab.domain.notification.entity.Notification;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationDto {

	private Long id;
	// private Long senderId;
	// private UserType senderType;
	private Long receiverId;
	private UserType receiverType;
	private NotificationType notificationType;
	// private String title;
	private String message;
	private Long relatedId;
	private Boolean isRead;
	private LocalDateTime createdAt;

	// 기존 호환성을 위한 메서드들
	public Long getUserId() {
		return receiverId;
	}

	public UserType getUserType() {
		return receiverType;
	}

	public void setUserId(Long userId) {
		this.receiverId = userId;
	}

	public void setUserType(UserType userType) {
		this.receiverType = userType;
	}

	public static NotificationDto of(Long receiverId, UserType receiverType,
		NotificationType notificationType,
		String message, Long relatedId) {
		return new NotificationDto(
			null, // id
			receiverId,
			receiverType,
			notificationType,
			message,
			relatedId,
			false, // isRead
			LocalDateTime.now() // createdAt
		);
	}

	// // 기존 호환성을 위한 간단한 of 메서드
	// public static NotificationDto of(Long userId, UserType userType,
	//                                NotificationType notificationType,
	//                                String title, String message,
	//                                String data, Long relatedId) {
	//     return NotificationDto.builder()
	//             .senderId(null) // 시스템 알림
	//             .senderType(null)
	//             .receiverId(userId)
	//             .receiverType(userType)
	//             .notificationType(notificationType)
	//             .title(title)
	//             .message(message)
	//             .data(data)
	//             .relatedId(relatedId)
	//             .isRead(false)
	//             .createdAt(LocalDateTime.now())
	//             .build();
	// }

	public static NotificationDto fromEntity(Notification notification) {
		return new NotificationDto(
			notification.getId(),
			notification.getReceiverId(),
			notification.getReceiverType(),
			notification.getNotificationType(),
			notification.getMessage(),
			notification.getRelatedId(),
			notification.getIsRead(),
			notification.getCreatedAt()
		);
	}

	public Notification toEntity() {
		return new Notification(
			receiverId,
			receiverType,
			notificationType,
			message,
			isRead,
			relatedId
		);
	}

	public static NotificationDto forSend(Notification notification) {
		return new NotificationDto(
			notification.getId(),
			notification.getReceiverId(),
			notification.getReceiverType(),
			notification.getNotificationType(),
			notification.getMessage(),
			notification.getRelatedId(),
			notification.getIsRead(),
			notification.getCreatedAt()
		);
	}

}
