package kernel.maidlab.domain.notification.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import kernel.maidlab.common.entity.TimeBase;
import kernel.maidlab.domain.notification.enums.NotificationType;
import kernel.maidlab.common.enums.UserType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "notifications")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Notification extends TimeBase {

	//@Column(name = "sender_id", nullable = true)
	//private Long senderId;

	// @Enumerated(EnumType.STRING)
	// @Column(name = "sender_type", nullable = true)
	// private UserType senderType;

	@Column(name = "receiver_id", nullable = false)
	private Long receiverId;

	@Enumerated(EnumType.STRING)
	@Column(name = "receiver_type", nullable = false)
	private UserType receiverType;

	@Enumerated(EnumType.STRING)
	@Column(name = "notification_type", nullable = false)
	private NotificationType notificationType;

	// @Column(name = "title", nullable = false)
	// private String title;

	@Column(name = "message", nullable = false, length = 1000)
	private String message;

	// @Column(name = "data", columnDefinition = "TEXT")
	// private String data; // JSON 형태의 추가 데이터

	@Column(name = "is_read", nullable = false)
	private Boolean isRead = false;

	@Column(name = "related_id")
	private Long relatedId; // 관련 예약 ID, 매칭 ID 등

	public void markAsRead() {
		this.isRead = true;
	}

	public static Notification of(Long receiverId, UserType receiverType, NotificationType notificationType,
		String message, Long relatedId) {
		return new Notification(
			receiverId,
			receiverType,
			notificationType,
			message,
			false, // isRead
			relatedId
		);
	}
}
