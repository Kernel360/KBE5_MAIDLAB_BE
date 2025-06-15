package kernel.maidlab.admin.event.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import kernel.maidlab.admin.event.dto.request.EventRequestDto;
import kernel.maidlab.admin.event.dto.response.EventListResponseDto;
import kernel.maidlab.admin.event.dto.response.EventResponseDto;
import kernel.maidlab.admin.event.service.EventService;
import kernel.maidlab.common.dto.ResponseDto;
import lombok.RequiredArgsConstructor;

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
	@PatchMapping("/event/{eventId}")
	public ResponseEntity<ResponseDto<Void>> updateEvent(@PathVariable Long eventId,
		@Valid @RequestBody EventRequestDto eventRequestDto, HttpServletRequest req) {
		return eventService.updateEvent(eventId, eventRequestDto, req);
	}

	@Override
	@DeleteMapping("/event/{eventId}")
	public ResponseEntity<ResponseDto<Void>> deleteEvent(@PathVariable Long eventId, HttpServletRequest req) {
		return eventService.deleteEvent(eventId, req);
	}
}
