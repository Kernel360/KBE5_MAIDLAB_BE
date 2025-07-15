package kernel.maidlab.notification;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import kernel.maidlab.domain.consumer.entity.Consumer;
import kernel.maidlab.domain.manager.entity.Manager;
import kernel.maidlab.domain.notification.dto.NotificationDto;
import kernel.maidlab.domain.notification.service.NotificationService;
import kernel.maidlab.domain.util.UserValidator;
import kernel.maidlab.common.dto.ResponseDto;
import kernel.maidlab.common.enums.NotificationType;
import kernel.maidlab.common.enums.ResponseType;
import kernel.maidlab.common.enums.UserType;
import kernel.maidlab.core.security.AuthenticationHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;


@Slf4j
@Tag(name = "알림", description = "사용자 알림 관련 API")
@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

	private final NotificationService notificationService;
	private final UserValidator userValidator;

	@Operation(summary = "SSE 연결", description = "사용자의 실시간 알림을 위한 SSE 연결을 생성합니다.")
	@CrossOrigin(origins = {"http://localhost:5173", "https://kbe-5-maidlab-fe.vercel.app",
		"https://api-maidlab.duckdns.org", "https://www.maidlab.site"})
	@GetMapping(value = "/connect", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
	public SseEmitter connect() {
		return notificationService.connect();
	}

	@Operation(summary = "SSE 연결 해제", description = "사용자의 SSE 연결을 해제합니다.")
	@PostMapping("/disconnect")
	public ResponseEntity<ResponseDto<String>> disconnect(HttpServletRequest request) {
		notificationService.disconnect(request);
		return ResponseDto.success(ResponseType.SUCCESS, "연결이 해제되었습니다.");
	}

	@Operation(summary = "읽지 않은 알림 조회", description = "사용자의 읽지 않은 알림 목록을 조회합니다.")
	@GetMapping("/unread")
	public ResponseEntity<ResponseDto<List<NotificationDto>>> getUnreadNotifications(HttpServletRequest request) {
		List<NotificationDto> notifications = notificationService.getUnreadNotifications(request);
		return ResponseDto.success(ResponseType.SUCCESS, notifications);
	}

	@Operation(summary = "전체 알림 조회", description = "사용자의 모든 알림 목록을 페이징으로 조회합니다.")
	@GetMapping
	public ResponseEntity<ResponseDto<Page<NotificationDto>>> getAllNotifications(
		HttpServletRequest request,
		@RequestParam(defaultValue = "0") int page,
		@RequestParam(defaultValue = "10") int size) {
		Pageable pageable = PageRequest.of(page, size);
		Page<NotificationDto> notifications = notificationService.getAllNotifications(request, pageable);
		return ResponseDto.success(ResponseType.SUCCESS, notifications);
	}

	@Operation(summary = "알림 읽음 처리", description = "특정 알림을 읽음으로 처리합니다.")
	@PostMapping("/{notificationId}/read")
	public ResponseEntity<ResponseDto<String>> markAsRead(HttpServletRequest request,
		@PathVariable Long notificationId) {
		notificationService.markAsRead(request, notificationId);
		return ResponseDto.success(ResponseType.SUCCESS, "알림이 읽음 처리되었습니다.");
	}

	@Operation(summary = "테스트 알림 전송", description = "테스트용 알림을 전송합니다.")
	@PostMapping("/test")
	public ResponseEntity<ResponseDto<String>> sendTestNotification(HttpServletRequest request) {
		UserType type = AuthenticationHelper.getCurrentUserType();
		String userId = AuthenticationHelper.getCurrentUserKey();

		Object user = userValidator.findByUuid(userId, type);
		Long id = switch (type) {
			case MANAGER -> ((Manager)user).getId();
			case CONSUMER -> ((Consumer)user).getId();
			default -> throw new IllegalArgumentException("지원하지 않는 사용자 타입: " + type);
		};

		log.info("테스트 알림 전송 요청 - 사용자 ID: {}, 타입: {}", id, type);

		NotificationDto testNotification = NotificationDto.of(
			// sender 정보 (시스템 알림)
			id, type, // receiver 정보
			NotificationType.SYSTEM_NOTICE,
			"테스트 메시지입니다.",
			null
		);

		notificationService.sendNotification(testNotification);

		return ResponseDto.success(ResponseType.SUCCESS, "테스트 알림이 전송되었습니다.");
	}
}
