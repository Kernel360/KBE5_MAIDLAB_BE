package kernel.maidlab.admin.event.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import kernel.maidlab.admin.auth.entity.Admin;
import kernel.maidlab.admin.auth.jwt.AdminJwtFilter;
import kernel.maidlab.admin.auth.repository.AdminRepository;
import kernel.maidlab.admin.event.dto.object.EventListItem;
import kernel.maidlab.admin.event.dto.request.EventRequestDto;
import kernel.maidlab.admin.event.dto.response.EventListResponseDto;
import kernel.maidlab.admin.event.dto.response.EventResponseDto;
import kernel.maidlab.admin.event.entity.Event;
import kernel.maidlab.admin.event.repository.EventRepository;
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
			event.getCreatedAt()
		);

		return ResponseDto.success(responseDto);
	}

	// 이벤트 생성
	@Override
	public ResponseEntity<ResponseDto<Void>> createEvent(EventRequestDto eventRequestDto, HttpServletRequest req) {
		String adminKey = (String) req.getAttribute(AdminJwtFilter.CURRENT_ADMIN_KEY_VALUE);

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

		return ResponseDto.success();
	}

	// 이벤트 수정
	@Override
	public ResponseEntity<ResponseDto<Void>> updateEvent(Long eventId, EventRequestDto eventRequestDto, HttpServletRequest req) {
		String adminKey = (String) req.getAttribute(AdminJwtFilter.CURRENT_ADMIN_KEY_VALUE);

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
		String adminKey = (String) req.getAttribute(AdminJwtFilter.CURRENT_ADMIN_KEY_VALUE);

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