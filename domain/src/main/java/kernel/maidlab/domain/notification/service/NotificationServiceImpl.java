package kernel.maidlab.domain.notification.service;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import kernel.maidlab.core.aop.annotation.exception.ExceptionHandler;
import kernel.maidlab.core.aop.annotation.exception.Fallback;
import kernel.maidlab.core.aop.annotation.exception.Retry;
import kernel.maidlab.core.aop.enums.LogLevel;
import kernel.maidlab.common.enums.ResponseType;

import jakarta.servlet.http.HttpServletRequest;
import kernel.maidlab.domain.notification.enums.NotificationType;
import kernel.maidlab.common.enums.Status;
import kernel.maidlab.common.enums.UserType;
import kernel.maidlab.core.security.AuthenticationHelper;
import kernel.maidlab.domain.consumer.entity.Consumer;
import kernel.maidlab.domain.manager.entity.Manager;
import kernel.maidlab.domain.notification.dto.NotificationDto;
import kernel.maidlab.domain.notification.entity.Notification;
import kernel.maidlab.domain.notification.repository.NotificationRepository;
import kernel.maidlab.domain.util.NotificationConnectionKey;
import kernel.maidlab.domain.util.UserValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

	private static final Long DEFAULT_TIMEOUT = 60L * 1000 * 60; // 1시간

	private final NotificationRepository notificationRepository;
	private final UserValidator userValidator;

	// 사용자별 SSE 연결 관리 (userId + userType 조합으로 구분)
	private final ConcurrentHashMap<NotificationConnectionKey, SseEmitter> connections = new ConcurrentHashMap<>();

	private UserType getCurrentUserType(HttpServletRequest request) {
		return AuthenticationHelper.getCurrentUserType();
	}

	private Long getCurrentUserId(HttpServletRequest request) {
		UserType type = getCurrentUserType(request);
		String userId = AuthenticationHelper.getCurrentUserKey();
		Object user = userValidator.findByUuid(userId, type);

		return switch (type) {
			case MANAGER -> ((Manager)user).getId();
			case CONSUMER -> ((Consumer)user).getId();
			default -> throw new IllegalArgumentException("지원하지 않는 사용자 타입: " + type);
		};
	}

	private NotificationConnectionKey createConnectionKey() {
		UserType type = AuthenticationHelper.getCurrentUserType();

		String uuid = AuthenticationHelper.getCurrentUserKey();
		Object user = userValidator.findByUuid(uuid, type);

		return switch (type) {
			case MANAGER -> NotificationConnectionKey.of(((Manager)user).getId(), type);
			case CONSUMER -> NotificationConnectionKey.of(((Consumer)user).getId(), type);
			case ADMIN -> null;//NotificationConnectionKey.of(((Admin)user).getId(), type);
		};
	}

	@Override
	@ExceptionHandler(
		value = {IOException.class},
		responseType = ResponseType.INTERNAL_SERVER_ERROR,
		message = "SSE 연결 처리 중 오류가 발생했습니다",
		logLevel = LogLevel.WARN
	)
	public SseEmitter connect() {
		NotificationConnectionKey connectionKey = createConnectionKey();

		SseEmitter emitter = new SseEmitter(DEFAULT_TIMEOUT);

		// 연결 완료 시 정리
		emitter.onCompletion(() -> {
			connections.remove(connectionKey, emitter);
		});

		// 연결 타임아웃 시 정리
		emitter.onTimeout(() -> {
			connections.remove(connectionKey, emitter);
		});

		// 연결 에러 시 정리
		emitter.onError((throwable) -> {
			log.debug("SSE 연결 에러 (클라이언트 연결 끊김) - 사용자: {}", connectionKey.toStringKey());
			connections.remove(connectionKey, emitter);
		});

		// 연결 확인을 위한 초기 메시지 전송
		try {
			emitter.send(SseEmitter.event()
				.name("connect")
				.data("Connected to notification service"));
		} catch (IOException e) {
			throw new RuntimeException("SSE 연결 초기화 실패", e);
		}

		// 초기 메시지 전송 성공 후에만 연결 저장
		SseEmitter oldEmitter = connections.put(connectionKey, emitter);
		if (oldEmitter != null) {
			try {
				oldEmitter.complete();
			} catch (Exception e) {
				log.debug("기존 SSE 연결 종료 중 오류 (정상) - 사용자: {}", connectionKey.toStringKey());
			}
		}
		return emitter;
	}

	@Override
	public void disconnect(HttpServletRequest request) {
		NotificationConnectionKey connectionKey = createConnectionKey();

		SseEmitter emitter = connections.remove(connectionKey);
		if (emitter != null) {
			emitter.complete();
			
		}
	}

	@Override
	@Transactional
	@Retry(
		maxAttempts = 2,
		delay = 500,
		retryFor = {IOException.class},
		noRetryFor = {IllegalArgumentException.class}
	)
	@Fallback(
		method = "logNotificationFailure",
		exceptions = {Exception.class}
	)
	@ExceptionHandler(
		value = {Exception.class},
		responseType = ResponseType.INTERNAL_SERVER_ERROR,
		message = "알림 전송 중 오류가 발생했습니다",
		logLevel = LogLevel.ERROR,
		enableNotification = true
	)
	public void sendNotification(NotificationDto notification) {
		// DB에 알림 저장
		Notification entity = notification.toEntity();
		System.out.println(entity.getNotificationType());
		Notification savedNotification = notificationRepository.save(entity);

		// 알림 수신자의 receiverType에 따라 연결 키 생성
		Long receiverId = notification.getReceiverId();
		UserType receiverType = notification.getReceiverType();
		NotificationConnectionKey connectionKey = NotificationConnectionKey.of(receiverId, receiverType);

		

		// SSE로 실시간 알림 전송
		SseEmitter emitter = connections.get(connectionKey);
		//System.out.println(emitter.toString());

		if (emitter != null) {
			try {
				NotificationDto forNotification = NotificationDto.forSend(savedNotification);
				emitter.send(SseEmitter.event()
					.name("notification")
					.data(forNotification));
			} catch (IOException e) {
				throw new RuntimeException("SSE 알림 전송 실패", e);
			}
		} else {
			log.warn("SSE 연결 없음 - 사용자: {}, 알림은 DB에 저장됨", connectionKey.toStringKey());
		}
	}

	@Override
	public List<NotificationDto> getUnreadNotifications(HttpServletRequest request) {
		Long id = getCurrentUserId(request);
		UserType type = getCurrentUserType(request);
		return notificationRepository.findByReceiverIdAndReceiverTypeAndIsReadFalseOrderByCreatedAtDesc(id,
				type)
			.stream()
			.map(NotificationDto::fromEntity)
			.toList();
	}

	@Override
	public Page<NotificationDto> getAllNotifications(HttpServletRequest request, Pageable pageable) {
		Long id = getCurrentUserId(request);
		UserType type = getCurrentUserType(request);
		return notificationRepository.findByReceiverIdAndReceiverTypeOrderByCreatedAtDesc(id, type, pageable)
			.map(NotificationDto::fromEntity);
	}

	@Override
	public Page<NotificationDto> getNotifications(HttpServletRequest request, Pageable pageable) {
		Long id = getCurrentUserId(request);
		UserType type = getCurrentUserType(request);
		return notificationRepository.findByReceiverIdAndReceiverTypeOrderByCreatedAtDesc(id, type, pageable)
			.map(NotificationDto::fromEntity);
	}

	@Override
	public List<NotificationDto> getNotificationsByType(HttpServletRequest request, NotificationType type) {
		Long id = getCurrentUserId(request);
		UserType userType = getCurrentUserType(request);
		return notificationRepository.findByReceiverIdAndReceiverTypeAndNotificationTypeOrderByCreatedAtDesc(
				id, userType, type)
			.stream()
			.map(NotificationDto::fromEntity)
			.toList();
	}

	@Override
	@Transactional
	public void markAsRead(HttpServletRequest request, Long notificationId) {
		Long id = getCurrentUserId(request);
		UserType type = getCurrentUserType(request);
		NotificationConnectionKey connectionKey = NotificationConnectionKey.of(id, type);

		// DB에서 알림 읽음 처리
		notificationRepository.findById(notificationId)
			.filter(notification -> notification.getReceiverId().equals(id) &&
				notification.getReceiverType().equals(type))
			.ifPresent(notification -> {
				notification.markAsRead();
				notificationRepository.save(notification);
			});
	}

	@Override
	@Transactional
	public void markAllAsRead(HttpServletRequest request) {
		NotificationConnectionKey connectionKey = createConnectionKey();
		Long id = getCurrentUserId(request);
		UserType type = getCurrentUserType(request);

		int updatedCount = notificationRepository.markAllAsReadByReceiverIdAndType(id, type);
	}

	@Override
	public int getUnreadCount(HttpServletRequest request) {
		Long id = getCurrentUserId(request);
		UserType type = getCurrentUserType(request);
		return notificationRepository.countByReceiverIdAndReceiverTypeAndIsReadFalse(id, type);
	}

	@Override
	public NotificationDto createMatchingNotification(Long managerId, Long matchingId,
		String consumerName, String serviceType) {

		// 사용자가 읽기 쉬운 메시지
		String message = String.format("%s님이 %s 서비스를 요청하였습니다.", consumerName, serviceType);

		return NotificationDto.of(
			managerId, UserType.MANAGER,
			NotificationType.MATCHING_REQUEST,
			message,
			matchingId
		);
	}

	@Override
	public NotificationDto createMatchingStatusNotification(Long consumerId, Long matchingId,
		String ManagerName, Status status) {

		// 사용자가 읽기 쉬운 메시지
		String message = (status == Status.APPROVED) ? String.format("%s님이 서비스를 승인하였습니다.", ManagerName) :
			String.format("%s님이 서비스를 거절하였습니다.\n새로운 매니저에게 요청이 전송됩니다.", ManagerName);
		NotificationType type =
			(status == Status.APPROVED) ? NotificationType.MATCHING_APPROVED : NotificationType.MATCHING_REJECTED;

		return NotificationDto.of(
			consumerId, UserType.CONSUMER,
			type,
			message,
			matchingId
		);
	}

	@Override
	public NotificationDto createReservationCancelNotification(Long managerId, Long reservationId,
		String consumerName) {
		String message = String.format("%s님이 예약을 취소 하였습니다.", consumerName);
		return NotificationDto.of(
			managerId, UserType.MANAGER,
			NotificationType.RESERVATION_CANCELLED,
			message,
			reservationId
		);
	}

	@Override
	public NotificationDto createReservationPaidNotification(Long managerId, Long reservationId,
		String consumerName) {
		String message = String.format("%s님이 결제를 완료하였습니다.", consumerName);
		return NotificationDto.of(
			managerId, UserType.MANAGER,
			NotificationType.PAYMENT_CONFIRMED,
			message,
			reservationId
		);
	}

	@Override
	public NotificationDto createReservationCheckInNotification(Long consumerId, Long reservationId,
		String managerName) {
		String message = String.format("%s님이 서비스 수행을 위해 체크인 하였습니다.", managerName);
		return NotificationDto.of(
			consumerId, UserType.CONSUMER,
			NotificationType.SERVICE_CHECKIN,
			message,
			reservationId
		);
	}

	@Override
	public NotificationDto createReservationCheckOutNotification(Long consumerId, Long reservationId,
		String managerName) {
		String message = String.format("%s님이 서비스 수행을 마치고 체크아웃 하였습니다.", managerName);
		return NotificationDto.of(
			consumerId, UserType.CONSUMER,
			NotificationType.SERVICE_COMPLETED,
			message,
			reservationId
		);
	}

	private void logNotificationFailure(NotificationDto notification) {
		log.error("알림 전송 실패로 인한 대체 처리 - 수신자 ID: {}, 타입: {}", 
			notification.getReceiverId(), notification.getNotificationType());
	}

}
