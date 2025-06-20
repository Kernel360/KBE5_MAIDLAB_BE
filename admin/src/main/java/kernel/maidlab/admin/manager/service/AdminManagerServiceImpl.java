package kernel.maidlab.admin.manager.service;

import java.util.Objects;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kernel.maidlab.admin.manager.repository.AdminManagerRepository;
import kernel.maidlab.common.dto.manager.ManagerListResponseDto;
import kernel.maidlab.common.dto.manager.ManagerResponseDto;
import kernel.maidlab.common.entity.manager.Manager;
import kernel.maidlab.common.enums.Status;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminManagerServiceImpl implements AdminManagerService {

	private final AdminManagerRepository adminManagerRepository;

	@Override
	public ManagerResponseDto getManager(Long id) {
		Manager manager = adminManagerRepository.findById(id).orElse(null);
		Objects.requireNonNull(manager);
		return ManagerResponseDto.builder()
			.uuid(manager.getUuid())
			.phoneNumber(manager.getPhoneNumber())
			.name(manager.getName())
			.birth(manager.getBirth())
			.gender(manager.getGender())
			.averageRate(manager.getAverageRate())
			.region(manager.getRegions())
			.isVerified(manager.getIsVerified())
			.isDeleted(manager.getIsDeleted())
			.build();
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
	public Page<ManagerListResponseDto> getManagerByPageWithStatus(int page, int size, Status status) {
		Pageable pageable = PageRequest.of(page, size);
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

}
