package kernel.maidlab.api.event.service;

import org.springframework.http.ResponseEntity;

import jakarta.servlet.http.HttpServletRequest;
import kernel.maidlab.api.event.dto.request.EventRequestDto;
import kernel.maidlab.api.event.dto.response.EventListResponseDto;
import kernel.maidlab.api.event.dto.response.EventResponseDto;
import kernel.maidlab.common.dto.ResponseDto;

public interface EventService {

	ResponseEntity<ResponseDto<EventListResponseDto>> getAllEvents();

	ResponseEntity<ResponseDto<EventResponseDto>> getEvent(Long eventId);

	ResponseEntity<ResponseDto<Void>> createEvent(EventRequestDto eventRequestDto, HttpServletRequest req);

	ResponseEntity<ResponseDto<Void>> updateEvent(Long eventId, EventRequestDto eventRequestDto, HttpServletRequest req);

	ResponseEntity<ResponseDto<Void>> deleteEvent(Long eventId, HttpServletRequest req);
}
