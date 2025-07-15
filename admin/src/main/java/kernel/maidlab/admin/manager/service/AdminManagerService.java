package kernel.maidlab.admin.manager.service;

import jakarta.servlet.http.HttpServletRequest;
import kernel.maidlab.api.manager.dto.ManagerListResponseDto;
import kernel.maidlab.api.manager.dto.response.AdminManagerResponseDto;
import kernel.maidlab.common.enums.Status;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;

public interface AdminManagerService {
	AdminManagerResponseDto getManager(Long id);

	@Transactional
	void rejectManager(Long managerId);

	@Transactional
	void approveManager(Long managerId);

	// Page<ManagerListResponseDto> getManagerByPageWithStatus(int page, int size, Status status, Boolean sortByRating, Boolean sortDescending);

	Page<ManagerListResponseDto> getManagerByPageWithStatus(int page, int size, Status status, boolean sortByRating,
															Boolean isDescending);

	Page<ManagerListResponseDto> getManagerBypage(int page, int size);

	Long managerCount(HttpServletRequest request);

	Long newManagerCount(HttpServletRequest request);
}
