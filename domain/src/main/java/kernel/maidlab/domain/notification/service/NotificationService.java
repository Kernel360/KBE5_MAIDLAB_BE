package kernel.maidlab.domain.notification.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import jakarta.servlet.http.HttpServletRequest;
import kernel.maidlab.common.enums.NotificationType;
import kernel.maidlab.common.enums.Status;
import kernel.maidlab.domain.notification.dto.NotificationDto;

public interface NotificationService {
	// SSE 연결 관리
	SseEmitter connect();

	void disconnect(HttpServletRequest request);

	// 알림 전송
	void sendNotification(NotificationDto notification);

	// 알림 조회
	List<NotificationDto> getUnreadNotifications(HttpServletRequest request);

	Page<NotificationDto> getAllNotifications(HttpServletRequest request, Pageable pageable);

	Page<NotificationDto> getNotifications(HttpServletRequest request, Pageable pageable);

	List<NotificationDto> getNotificationsByType(HttpServletRequest request, NotificationType type);

	// 알림 상태 관리
	void markAsRead(HttpServletRequest request, Long notificationId);

	void markAllAsRead(HttpServletRequest request);

	int getUnreadCount(HttpServletRequest request);

	// //매칭관련 알림 dto 생성
	// NotificationDto createMatchingNotification(Long managerId, Long reservationId,
	//     String serviceType,
	//     String address, LocalDateTime serviceStartTime,
	//     LocalDateTime serviceEndTime);

	NotificationDto createMatchingNotification(Long managerId, Long matchingId,
		String consumerName, String serviceType);

	NotificationDto createMatchingStatusNotification(Long consumerId, Long matchingId,
		String ManagerName, Status status);

	NotificationDto createReservationCancelNotification(Long managerId, Long reservationId,
		String consumerName);

	NotificationDto createReservationPaidNotification(Long managerId, Long reservationId,
		String consumerName);

	NotificationDto createReservationCheckInNotification(Long consumerId, Long reservationId,
		String managerName);

	NotificationDto createReservationCheckOutNotification(Long consumerId, Long reservationId,
		String managerName);
}
