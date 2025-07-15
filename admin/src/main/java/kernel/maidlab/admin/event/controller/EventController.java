package kernel.maidlab.admin.event.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import kernel.maidlab.admin.event.dto.request.EventRequestDto;
import kernel.maidlab.admin.event.dto.response.EventListResponseDto;
import kernel.maidlab.admin.event.dto.response.EventResponseDto;
import kernel.maidlab.admin.event.service.EventService;
import kernel.maidlab.common.dto.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class EventController implements EventApi {

	private final EventService eventService;

	@Override
	@GetMapping("/events")
	public ResponseEntity<ResponseDto<EventListResponseDto>> getAllEvents() {
		return eventService.getAllEvents();
	}

	@Override
	@GetMapping("/event/{eventId}")
	public ResponseEntity<ResponseDto<EventResponseDto>> getEventById(@PathVariable Long eventId) {
		return eventService.getEvent(eventId);
	}

	@Override
	@PostMapping("/admin/event")
	public ResponseEntity<ResponseDto<Void>> createEvent(@Valid @RequestBody EventRequestDto eventRequestDto,
		HttpServletRequest req) {
		return eventService.createEvent(eventRequestDto, req);
	}

	@Override
	@PatchMapping("/admin/event/{eventId}")
	public ResponseEntity<ResponseDto<Void>> updateEvent(@PathVariable Long eventId,
														 @Valid @RequestBody EventRequestDto eventRequestDto, HttpServletRequest req) {
		return eventService.updateEvent(eventId, eventRequestDto, req);
	}

	@Override
	@DeleteMapping("/admin/event/{eventId}")
	public ResponseEntity<ResponseDto<Void>> deleteEvent(@PathVariable Long eventId, HttpServletRequest req) {
		return eventService.deleteEvent(eventId, req);
	}

	@GetMapping("/eventcount")
	@Override
	public ResponseEntity<ResponseDto<Long>> eventCount(HttpServletRequest request) {
		return eventService.eventCount(request);
	}
}
