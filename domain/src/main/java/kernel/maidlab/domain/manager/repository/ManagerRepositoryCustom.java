package kernel.maidlab.domain.manager.repository;

import java.time.LocalDateTime;
import java.util.List;

import kernel.maidlab.domain.consumer.entity.Consumer;
import kernel.maidlab.domain.matching.dto.response.AvailableManagerResponseDto;

public interface ManagerRepositoryCustom {
	List<AvailableManagerResponseDto> findAvailableManagers(String site, LocalDateTime start, LocalDateTime end);

	List<AvailableManagerResponseDto> previousManagers(Consumer consumer);
}
