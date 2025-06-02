package kernel.maidlab.api.manager.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import kernel.maidlab.api.auth.entity.Manager;
import kernel.maidlab.api.manager.repository.ManagerRepository;
import kernel.maidlab.api.consumer.dto.response.ConsumerListResponseDto;
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

	//아직 쓰이는 곳이 없어 주석처리 합니다.
	// @Override
	// public Long GetIdByUuid(String uuid){
	// 	Manager uuidmanager = managerRepository.findByUuid(uuid)
	// 		.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 uuid입니다."));
	// 	return uuidmanager.getId();
	// }
	//
	// public String GetUuidByid(Long id){
	// 	Optional<Manager> manager = managerRepository.findById(id);
	// 	return manager.get().getUuid();
	// }


}
