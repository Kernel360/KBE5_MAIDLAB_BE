package kernel.maidlab.admin.manager.service;

import jakarta.servlet.http.HttpServletRequest;
import kernel.maidlab.admin.manager.repository.AdminManagerRepository;
import kernel.maidlab.domain.manager.dto.ManagerListResponseDto;
import kernel.maidlab.domain.manager.dto.response.AdminManagerResponseDto;
import kernel.maidlab.domain.manager.entity.Manager;
import kernel.maidlab.common.enums.Status;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class AdminManagerServiceImpl implements AdminManagerService {

	private final AdminManagerRepository adminManagerRepository;

	@Override
	public AdminManagerResponseDto getManager(Long id) {
		Manager manager = adminManagerRepository.findById(id).orElse(null);
		Objects.requireNonNull(manager);
		return AdminManagerResponseDto.getInstance(manager);
	}

	@Transactional
	@Override
	public void rejectManager(Long managerId) {
		Manager manager = adminManagerRepository.findById(managerId).orElse(null);
		Objects.requireNonNull(manager).reject();
	}

	@Transactional
	@Override
	public void approveManager(Long managerId) {
		Manager manager = adminManagerRepository.findById(managerId).orElse(null);
		Objects.requireNonNull(manager).approve();
	}

	@Override
	public Page<ManagerListResponseDto> getManagerByPageWithStatus(int page, int size, Status status,
																   boolean sortByRating, Boolean isDescending) {
		Pageable pageable;

		if (sortByRating) {
			if (isDescending)
				pageable = PageRequest.of(page, size, Sort.by("averageRate").descending());
			else
				pageable = PageRequest.of(page, size, Sort.by("averageRate").ascending());
		} else {
			pageable = PageRequest.of(page, size);
		}
		return adminManagerRepository.findAllByIsVerified(status, pageable)
			.map(manager -> new ManagerListResponseDto(
				manager.getName(),
				manager.getUuid(),
				manager.getId()
			));
	}

	@Override
	public Page<ManagerListResponseDto> getManagerBypage(int page, int size) {
		Pageable pageable = PageRequest.of(page, size);
		return adminManagerRepository.findAll(pageable)
			.map(manager -> new ManagerListResponseDto(
				manager.getName(),
				manager.getUuid(),
				manager.getId()
			));
	}

	@Override
	public Long managerCount(HttpServletRequest request) {
		return adminManagerRepository.countByIsDeletedFalseAndIsVerified(Status.APPROVED);
	}

	@Override
	public Long newManagerCount(HttpServletRequest request) {
		return adminManagerRepository.countByIsDeletedFalseAndIsVerified(Status.PENDING);
	}

}
