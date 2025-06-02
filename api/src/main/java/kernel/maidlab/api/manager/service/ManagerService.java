package kernel.maidlab.api.manager.service;

import org.springframework.data.domain.Page;

import kernel.maidlab.api.manager.dto.ManagerListResponseDto;

public interface ManagerService {
	Page<ManagerListResponseDto> getManagerBypage(int page, int size);
	// Long GetIdByUuid(String uuid);
}
