package kernel.maidlab.admin.consumer.service;

import kernel.maidlab.admin.consumer.repository.AdminConsumerRepository;
import kernel.maidlab.domain.consumer.dto.response.AdminConsumerProfileResponseDto;
import kernel.maidlab.domain.consumer.dto.response.ConsumerListResponseDto;
import kernel.maidlab.domain.consumer.entity.Consumer;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminConsumerServiceImpl implements AdminConsumerService {

	private final AdminConsumerRepository adminConsumerRepository;

	// 관리자용 전체조회로직
	@Override
	public Page<ConsumerListResponseDto> getConsumerBypage(int page, int size) {
		Pageable pageable = PageRequest.of(page, size);
		return adminConsumerRepository.findAll(pageable)
			.map(consumer -> new ConsumerListResponseDto(
				consumer.getId(),
				consumer.getPhoneNumber(),
				consumer.getName(),
				consumer.getUuid()
			));
	}

	@Override
	public AdminConsumerProfileResponseDto getConsumerProfileById(Long id) {
		Consumer consumer = adminConsumerRepository.findById(id)
			.orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

		return AdminConsumerProfileResponseDto.from(consumer);
	}

	@Override
	public Long getCount() {
		return adminConsumerRepository.countByIsDeletedFalse();
	}
}
