package kernel.maidlab.api.manager.repository;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.servlet.http.HttpServletRequest;
import kernel.maidlab.common.dto.matching.response.AvailableManagerResponseDto;
import kernel.maidlab.common.entity.consumer.Consumer;

public interface ManagerRepositoryCustom {
	List<AvailableManagerResponseDto> findAvailableManagers(String site, LocalDateTime start, LocalDateTime end);

	List<AvailableManagerResponseDto> previousManagers(Consumer consumer);
}
