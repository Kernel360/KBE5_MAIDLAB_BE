package kernel.maidlab.admin.consumer.controller;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import kernel.maidlab.admin.consumer.service.AdminConsumerService;
import kernel.maidlab.common.dto.consumer.response.ConsumerListResponseDto;
import kernel.maidlab.common.dto.consumer.response.ConsumerProfileResponseDto;
import kernel.maidlab.api.consumer.service.ConsumerService;
import kernel.maidlab.common.dto.ResponseDto;
import kernel.maidlab.common.enums.ResponseType;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/admin/consumer")
public class AdminConsumerController implements AdminConsumerApi {

	private final AdminConsumerService adminConsumerService;

	@GetMapping
	@Override
	public ResponseEntity<ResponseDto<Page<ConsumerListResponseDto>>> getConsumers(
		@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
		return ResponseDto.success(ResponseType.SUCCESS, adminConsumerService.getConsumerBypage(page, size));
	}

	@GetMapping("/{consumerId}")
	@Override
	public ResponseEntity<ResponseDto<ConsumerProfileResponseDto>> getConsumer(
		@PathVariable("consumerId") Long consumerId) {
		return ResponseDto.success(ResponseType.SUCCESS, adminConsumerService.getConsumerProfileById((Long)consumerId));
	}

}
