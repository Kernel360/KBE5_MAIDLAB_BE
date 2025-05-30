package kernel.maidlab.api.manager.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import kernel.maidlab.api.auth.entity.Manager;
import kernel.maidlab.api.matching.dto.response.AvailableManagerResponseDto;

public interface ManagerRepositoryCustom {
	List<AvailableManagerResponseDto> FindAvailableManagers(String site, LocalDateTime Start, LocalDateTime End);
}
