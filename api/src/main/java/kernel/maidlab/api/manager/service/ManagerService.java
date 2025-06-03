package kernel.maidlab.api.manager.service;

import org.springframework.data.domain.Page;

import kernel.maidlab.api.manager.dto.ManagerListResponseDto;
import kernel.maidlab.api.manager.dto.ManagerResponseDto;

public interface ManagerService {
	Page<ManagerListResponseDto> getManagerBypage(int page, int size);
	ManagerResponseDto getManager(Long id);
	// Long GetIdByUuid(String uuid);
}
