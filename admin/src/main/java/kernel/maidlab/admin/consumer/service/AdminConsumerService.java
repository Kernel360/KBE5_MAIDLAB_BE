package kernel.maidlab.admin.consumer.service;

import org.springframework.data.domain.Page;

import kernel.maidlab.domain.consumer.dto.response.AdminConsumerProfileResponseDto;
import kernel.maidlab.domain.consumer.dto.response.ConsumerListResponseDto;

public interface AdminConsumerService {
	// 관리자용 전체조회로직
	Page<ConsumerListResponseDto> getConsumerBypage(int page, int size);

	AdminConsumerProfileResponseDto getConsumerProfileById(Long id);

	Long getCount();

	void deleteConsumer(Long consumerId);
}
