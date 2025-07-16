package kernel.maidlab.admin.event.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import kernel.maidlab.core.aop.annotation.exception.ExceptionHandler;
import kernel.maidlab.core.aop.annotation.exception.Retry;
import kernel.maidlab.core.aop.enums.LogLevel;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import kernel.maidlab.admin.auth.entity.Admin;
import kernel.maidlab.admin.auth.repository.AdminRepository;
import kernel.maidlab.admin.event.dto.object.EventListItem;
import kernel.maidlab.admin.event.dto.request.EventRequestDto;
import kernel.maidlab.admin.event.dto.response.EventListResponseDto;
import kernel.maidlab.admin.event.dto.response.EventResponseDto;
import kernel.maidlab.admin.event.entity.Event;
import kernel.maidlab.admin.event.repository.EventRepository;
import kernel.maidlab.common.dto.ResponseDto;
import kernel.maidlab.common.enums.ResponseType;
import kernel.maidlab.core.exception.BaseException;
import kernel.maidlab.core.security.AuthenticationHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class EventServiceImpl implements EventService {

	private final EventRepository eventRepository;
	private final AdminRepository adminRepository;

	// 이벤트 전체 조회
	@Override
	public ResponseEntity<ResponseDto<EventListResponseDto>> getAllEvents() {
		List<Event> events = eventRepository.findAllEventsOrderByCreatedAtDesc();

		List<EventListItem> eventListItems = events.stream()
			.map(event -> new EventListItem(
				event.getId(),
				event.getTitle(),
				event.getMainImageUrl(),
				event.getCreatedAt()
			))
			.collect(Collectors.toList());

		EventListResponseDto responseDto = new EventListResponseDto(eventListItems);

		return ResponseDto.success(responseDto);
	}

	// 이벤트 상세 조회
	@Override
	@ExceptionHandler(
		value = {RuntimeException.class},
		responseType = ResponseType.THIS_RESOURCE_DOES_NOT_EXIST,
		message = "이벤트를 찾을 수 없습니다",
		logLevel = LogLevel.WARN
	)
	public ResponseEntity<ResponseDto<EventResponseDto>> getEvent(Long eventId) {
		Event event = eventRepository.findById(eventId)
			.orElseThrow(() -> new BaseException(ResponseType.THIS_RESOURCE_DOES_NOT_EXIST));

		EventResponseDto responseDto = new EventResponseDto(
			event.getId(),
			event.getTitle(),
			event.getMainImageUrl(),
			event.getImageUrl(),
			event.getContent(),
			event.getCreatedAt(),
			event.getUpdatedAt()
		);

		return ResponseDto.success(responseDto);
	}

	// 이벤트 생성
	@Override
	@Retry(
		maxAttempts = 3,
		delay = 1000,
		retryFor = {org.springframework.dao.DataAccessException.class}
	)
	@ExceptionHandler(
		value = {RuntimeException.class},
		responseType = ResponseType.DATABASE_ERROR,
		message = "이벤트 생성 중 오류가 발생했습니다",
		logLevel = LogLevel.ERROR
	)
	public ResponseEntity<ResponseDto<Void>> createEvent(EventRequestDto eventRequestDto, HttpServletRequest req) {
		String adminKey = AuthenticationHelper.getCurrentUserKey();

		Admin admin = adminRepository.findByAdminKeyAndIsDeletedFalse(adminKey)
			.orElseThrow(() -> new BaseException(ResponseType.AUTHORIZATION_FAILED));

		Event event = Event.createEvent(
			admin.getId(),
			eventRequestDto.getTitle(),
			eventRequestDto.getMainImageUrl(),
			eventRequestDto.getImageUrl(),
			eventRequestDto.getContent()
		);

		eventRepository.save(event);
		log.info("이벤트 생성 완료 - 이벤트 ID: {}, 제목: {}", event.getId(), event.getTitle());

		return ResponseDto.success();
	}

	// 이벤트 수정
	@Override
	@ExceptionHandler(
		value = {RuntimeException.class},
		responseType = ResponseType.DATABASE_ERROR,
		message = "이벤트 수정 중 오류가 발생했습니다",
		logLevel = LogLevel.WARN
	)
	public ResponseEntity<ResponseDto<Void>> updateEvent(Long eventId, EventRequestDto eventRequestDto,
		HttpServletRequest req) {
		String adminKey = AuthenticationHelper.getCurrentUserKey();

		Admin admin = adminRepository.findByAdminKeyAndIsDeletedFalse(adminKey)
			.orElseThrow(() -> new BaseException(ResponseType.AUTHORIZATION_FAILED));

		Event event = eventRepository.findById(eventId)
			.orElseThrow(() -> new BaseException(ResponseType.THIS_RESOURCE_DOES_NOT_EXIST));

		event.updateEvent(
			eventRequestDto.getTitle(),
			eventRequestDto.getMainImageUrl(),
			eventRequestDto.getImageUrl(),
			eventRequestDto.getContent()
		);

		return ResponseDto.success();
	}

	// 이벤트 삭제 (물리 삭제)
	@Override
	@ExceptionHandler(
		value = {RuntimeException.class},
		responseType = ResponseType.DATABASE_ERROR,
		message = "이벤트 삭제 중 오류가 발생했습니다",
		logLevel = LogLevel.WARN
	)
	public ResponseEntity<ResponseDto<Void>> deleteEvent(Long eventId, HttpServletRequest req) {
		String adminKey = AuthenticationHelper.getCurrentUserKey();

		Admin admin = adminRepository.findByAdminKeyAndIsDeletedFalse(adminKey)
			.orElseThrow(() -> new BaseException(ResponseType.AUTHORIZATION_FAILED));

		Event event = eventRepository.findById(eventId)
			.orElseThrow(() -> new BaseException(ResponseType.THIS_RESOURCE_DOES_NOT_EXIST));

		eventRepository.delete(event);

		return ResponseDto.success();
	}

	@Override
	public ResponseEntity<ResponseDto<Long>> eventCount(HttpServletRequest request) {
		return ResponseDto.success(eventRepository.count());
	}
}
