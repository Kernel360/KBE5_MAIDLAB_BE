package kernel.maidlab.api.manager.repository;

import java.time.LocalDateTime;
import java.util.List;

import kernel.maidlab.api.matching.dto.ManagerResponseDto;

public interface ManagerRepositoryCustom {
	List<ManagerResponseDto> FindAvailableManagers(String site, LocalDateTime Start, LocalDateTime End);
}
