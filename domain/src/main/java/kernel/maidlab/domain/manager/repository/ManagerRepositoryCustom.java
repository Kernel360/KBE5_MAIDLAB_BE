package kernel.maidlab.domain.manager.repository;

import kernel.maidlab.domain.consumer.entity.Consumer;
import kernel.maidlab.domain.matching.dto.response.AvailableManagerResponseDto;

import java.time.LocalDateTime;
import java.util.List;

public interface ManagerRepositoryCustom {
    List<AvailableManagerResponseDto> findAvailableManagers(String site, LocalDateTime start, LocalDateTime end);

	AvailableManagerResponseDto findRandomAvailableManagers(String gu, LocalDateTime start, LocalDateTime end);

	List<AvailableManagerResponseDto> previousManagers(Consumer consumer);
}
