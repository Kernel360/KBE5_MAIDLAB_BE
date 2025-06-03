package kernel.maidlab.api.manager.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import kernel.maidlab.api.auth.entity.Manager;
import kernel.maidlab.api.manager.dto.ManagerResponseDto;
import kernel.maidlab.api.manager.repository.ManagerRepository;
import kernel.maidlab.api.manager.dto.ManagerListResponseDto;

@Service
public class ManagerServiceImpl implements ManagerService {

	private final ManagerRepository managerRepository;

	public ManagerServiceImpl(ManagerRepository managerRepository) {
		this.managerRepository = managerRepository;
	}

	@Override
	public Page<ManagerListResponseDto> getManagerBypage(int page, int size) {
		Pageable pageable = PageRequest.of(page, size);
		return managerRepository.findAll(pageable)
			.map(manager -> new ManagerListResponseDto(
				manager.getName(),
				manager.getUuid(),
				manager.getId()
			));
	}

	@Override
	public ManagerResponseDto getManager(Long id){
		Manager manager = managerRepository.findById(id).orElse(null);
		System.out.println(manager.getId());
		ManagerResponseDto managerResponseDto = ManagerResponseDto.builder()
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
		return managerResponseDto;
	}
	@Transactional
	@Override
	public void approveManager(Long managerId) {
		Manager manager = managerRepository.findById(managerId).orElse(null);
		manager.approve();
	}

	@Transactional
	@Override
	public void rejectManager(Long managerId) {
		Manager manager = managerRepository.findById(managerId).orElse(null);
		manager.reject();
	}

}
