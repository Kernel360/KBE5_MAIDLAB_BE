package kernel.maidlab.admin.consumer.service;

import kernel.maidlab.common.dto.consumer.response.AdminConsumerProfileResponseDto;
import org.springframework.data.domain.Page;

import kernel.maidlab.common.dto.consumer.response.ConsumerListResponseDto;

public interface AdminConsumerService {
	// 관리자용 전체조회로직
	Page<ConsumerListResponseDto> getConsumerBypage(int page, int size);

	AdminConsumerProfileResponseDto getConsumerProfileById(Long id);

	Long getCount();
}
