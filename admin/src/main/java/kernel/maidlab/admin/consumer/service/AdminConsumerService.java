package kernel.maidlab.admin.consumer.service;

import org.springframework.data.domain.Page;

import jakarta.servlet.http.HttpServletRequest;
import kernel.maidlab.common.dto.consumer.response.ConsumerListResponseDto;
import kernel.maidlab.common.dto.consumer.response.ConsumerProfileResponseDto;

public interface AdminConsumerService {
	// 관리자용 전체조회로직
	Page<ConsumerListResponseDto> getConsumerBypage(int page, int size);

	ConsumerProfileResponseDto getConsumerProfileById(Long id);

	Long getCount(HttpServletRequest request);
}
