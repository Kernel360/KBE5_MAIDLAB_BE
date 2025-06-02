package kernel.maidlab.api.manager.repository;

import java.time.LocalDateTime;
import java.util.List;

import kernel.maidlab.api.matching.dto.response.AvailableManagerResponseDto;

public interface ManagerRepositoryCustom {
	List<AvailableManagerResponseDto> findAvailableManagers(String site, LocalDateTime start, LocalDateTime end);
}
