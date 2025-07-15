package kernel.maidlab.api.manager.repository;

import kernel.maidlab.api.consumer.entity.Consumer;
import kernel.maidlab.api.matching.dto.response.AvailableManagerResponseDto;

import java.time.LocalDateTime;
import java.util.List;


public interface ManagerRepositoryCustom {
	List<AvailableManagerResponseDto> findAvailableManagers(String site, LocalDateTime start, LocalDateTime end);

	List<AvailableManagerResponseDto> previousManagers(Consumer consumer);
}
