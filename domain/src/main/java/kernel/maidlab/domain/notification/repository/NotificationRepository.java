package kernel.maidlab.domain.notification.repository;

import kernel.maidlab.domain.notification.entity.Notification;
import kernel.maidlab.common.enums.NotificationType;
import kernel.maidlab.common.enums.UserType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

	// 특정 사용자의 모든 알림 조회 (페이징)
	Page<Notification> findByReceiverIdOrderByCreatedAtDesc(Long receiverId, Pageable pageable);

	// 특정 사용자(ID + 타입)의 모든 알림 조회 (페이징)
	Page<Notification> findByReceiverIdAndReceiverTypeOrderByCreatedAtDesc(Long receiverId, UserType receiverType,
		Pageable pageable);

	// 특정 사용자의 특정 타입 알림 조회
	List<Notification> findByReceiverIdAndNotificationTypeOrderByCreatedAtDesc(Long receiverId,
		NotificationType notificationType);

	// 특정 사용자(ID + 타입)의 특정 알림 타입 조회
	List<Notification> findByReceiverIdAndReceiverTypeAndNotificationTypeOrderByCreatedAtDesc(Long receiverId,
		UserType receiverType, NotificationType notificationType);

	// 특정 사용자의 읽지 않은 알림 개수
	int countByReceiverIdAndIsReadFalse(Long receiverId);

	// 특정 사용자(ID + 타입)의 읽지 않은 알림 개수
	int countByReceiverIdAndReceiverTypeAndIsReadFalse(Long receiverId, UserType receiverType);

	// 특정 사용자의 알림을 모두 읽음 처리
	@Modifying
	@Query("UPDATE Notification n SET n.isRead = true WHERE n.receiverId = :receiverId AND n.isRead = false")
	int markAllAsReadByReceiverId(@Param("receiverId") Long receiverId);

	// 특정 사용자(ID + 타입)의 알림을 모두 읽음 처리
	@Modifying
	@Query("UPDATE Notification n SET n.isRead = true WHERE n.receiverId = :receiverId AND n.receiverType = :receiverType AND n.isRead = false")
	int markAllAsReadByReceiverIdAndType(@Param("receiverId") Long receiverId,
		@Param("receiverType") UserType receiverType);

	// 특정 사용자 타입의 알림 조회
	List<Notification> findByReceiverTypeOrderByCreatedAtDesc(UserType receiverType);

	// 관련 ID로 알림 조회 (예: 예약 ID로 관련 알림들 조회)
	List<Notification> findByRelatedIdOrderByCreatedAtDesc(Long relatedId);

	// 특정 사용자의 특정 관련 ID 알림 조회
	List<Notification> findByReceiverIdAndRelatedIdOrderByCreatedAtDesc(Long receiverId, Long relatedId);

	// 특정 사용자의 읽지 않은 알림 조회
	List<Notification> findByReceiverIdAndReceiverTypeAndIsReadFalseOrderByCreatedAtDesc(Long id, UserType type);
}
