package kernel.maidlab.api.event.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import kernel.maidlab.admin.auth.entity.Admin;
import kernel.maidlab.admin.auth.jwt.AdminJwtDto;
import kernel.maidlab.admin.auth.jwt.AdminJwtProvider;
import kernel.maidlab.admin.auth.repository.AdminRepository;
import kernel.maidlab.api.event.dto.object.EventListItem;
import kernel.maidlab.api.event.dto.request.EventRequestDto;
import kernel.maidlab.api.event.dto.response.EventListResponseDto;
import kernel.maidlab.api.event.dto.response.EventResponseDto;
import kernel.maidlab.api.event.entity.Event;
import kernel.maidlab.api.event.repository.EventRepository;
import kernel.maidlab.api.exception.BaseException;
import kernel.maidlab.common.dto.ResponseDto;
import kernel.maidlab.common.enums.ResponseType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class EventServiceImpl implements EventService {

	private final EventRepository eventRepository;
	private final AdminRepository adminRepository;
	private final AdminJwtProvider adminJwtProvider;

	// 이벤트 전체 조회
	@Override
	public ResponseEntity<ResponseDto<EventListResponseDto>> getAllEvents() {
		List<Event> events = eventRepository.findAllEventsOrderByCreatedAtDesc();

		List<EventListItem> eventListItems = events.stream()
			.map(event -> new EventListItem(
				event.getId(),
				event.getTitle(),
				event.getMainImageUrl(),
				event.getCreateAt()
			))
			.collect(Collectors.toList());

		EventListResponseDto responseDto = new EventListResponseDto(eventListItems);

		return ResponseDto.success(responseDto);
	}

	// 이벤트 상세 조회
	@Override
	public ResponseEntity<ResponseDto<EventResponseDto>> getEvent(Long eventId) {
		Event event = eventRepository.findById(eventId)
			.orElseThrow(() -> {
				throw new BaseException(ResponseType.THIS_RESOURCE_DOES_NOT_EXIST);
			});

		EventResponseDto responseDto = new EventResponseDto(
			event.getId(),
			event.getTitle(),
			event.getImageUrl(),
			event.getContent(),
			event.getCreateAt()
		);

		return ResponseDto.success(responseDto);
	}

	// 이벤트 생성
	@Override
	public ResponseEntity<ResponseDto<Void>> createEvent(EventRequestDto eventRequestDto, HttpServletRequest req) {
		String accessToken = adminJwtProvider.extractToken(req);

		if (accessToken == null) {
			throw new BaseException(ResponseType.AUTHORIZATION_FAILED);
		}

		AdminJwtDto.AdminValidationResult validationResult = adminJwtProvider.validateAdminAccessToken(accessToken);

		if (!validationResult.isValid()) {
			throw new BaseException(ResponseType.AUTHORIZATION_FAILED);
		}

		String adminKey = validationResult.getAdminKey();

		Admin admin = adminRepository.findByAdminKeyAndIsDeletedFalse(adminKey)
			.orElseThrow(() -> {
				throw new BaseException(ResponseType.AUTHORIZATION_FAILED);
			});

		Event event = Event.createEvent(
			admin,
			eventRequestDto.getTitle(),
			eventRequestDto.getMainImageUrl(),
			eventRequestDto.getImageUrl(),
			eventRequestDto.getContent()
		);

		eventRepository.save(event);

		log.info("이벤트 생성 완료 - AdminKey: {}, Title: {}", adminKey, eventRequestDto.getTitle());
		return ResponseDto.success();
	}

	// 이벤트 수정
	@Override
	public ResponseEntity<ResponseDto<Void>> updateEvent(Long eventId, EventRequestDto eventRequestDto, HttpServletRequest req) {
		String accessToken = adminJwtProvider.extractToken(req);

		if (accessToken == null) {
			throw new BaseException(ResponseType.AUTHORIZATION_FAILED);
		}

		AdminJwtDto.AdminValidationResult validationResult = adminJwtProvider.validateAdminAccessToken(accessToken);

		if (!validationResult.isValid()) {
			throw new BaseException(ResponseType.AUTHORIZATION_FAILED);
		}

		String adminKey = validationResult.getAdminKey();

		Admin admin = adminRepository.findByAdminKeyAndIsDeletedFalse(adminKey)
			.orElseThrow(() -> {
				throw new BaseException(ResponseType.AUTHORIZATION_FAILED);
			});

		Event event = eventRepository.findById(eventId)
			.orElseThrow(() -> {
				throw new BaseException(ResponseType.THIS_RESOURCE_DOES_NOT_EXIST);
			});

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
	public ResponseEntity<ResponseDto<Void>> deleteEvent(Long eventId, HttpServletRequest req) {
		String accessToken = adminJwtProvider.extractToken(req);

		if (accessToken == null) {
			throw new BaseException(ResponseType.AUTHORIZATION_FAILED);
		}

		AdminJwtDto.AdminValidationResult validationResult = adminJwtProvider.validateAdminAccessToken(accessToken);

		if (!validationResult.isValid()) {
			throw new BaseException(ResponseType.AUTHORIZATION_FAILED);
		}

		String adminKey = validationResult.getAdminKey();

		Admin admin = adminRepository.findByAdminKeyAndIsDeletedFalse(adminKey)
			.orElseThrow(() -> {
				throw new BaseException(ResponseType.AUTHORIZATION_FAILED);
			});

		Event event = eventRepository.findById(eventId)
			.orElseThrow(() -> {
				throw new BaseException(ResponseType.THIS_RESOURCE_DOES_NOT_EXIST);
			});

		eventRepository.delete(event);

		return ResponseDto.success();
	}
}