package kernel.maidlab.reservation.service;

import java.time.LocalDateTime;
import java.util.List;

import kernel.maidlab.reservation.dto.response.AvailableManagerDto;

public interface ReservationService {
	List<AvailableManagerDto> getAvailableManagersList(LocalDateTime startTime);
}
