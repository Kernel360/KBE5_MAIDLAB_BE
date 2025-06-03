package kernel.maidlab.api.manager.service;

import org.springframework.data.domain.Page;

import jakarta.transaction.Transactional;
import kernel.maidlab.api.manager.dto.ManagerListResponseDto;
import kernel.maidlab.api.manager.dto.ManagerResponseDto;

public interface ManagerService {
	Page<ManagerListResponseDto> getManagerBypage(int page, int size);

	ManagerResponseDto getManager(Long id);

	void approveManager(Long managerId);

	@Transactional
	void rejectManager(Long managerId);
	// Long GetIdByUuid(String uuid);
}
