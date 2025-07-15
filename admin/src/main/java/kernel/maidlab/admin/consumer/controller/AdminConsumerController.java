package kernel.maidlab.admin.consumer.controller;

import kernel.maidlab.admin.consumer.service.AdminConsumerService;
import kernel.maidlab.domain.consumer.dto.response.AdminConsumerProfileResponseDto;
import kernel.maidlab.domain.consumer.dto.response.ConsumerListResponseDto;
import kernel.maidlab.common.dto.ResponseDto;
import kernel.maidlab.common.enums.ResponseType;
import kernel.maidlab.core.aop.annotation.auth.AdminRequired;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/admin/consumer")
public class AdminConsumerController implements AdminConsumerApi {

	private final AdminConsumerService adminConsumerService;

	@GetMapping
	@AdminRequired
	@Override
	public ResponseEntity<ResponseDto<Page<ConsumerListResponseDto>>> getConsumers(
		@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
		return ResponseDto.success(ResponseType.SUCCESS, adminConsumerService.getConsumerBypage(page, size));
	}

	@GetMapping("/{consumerId}")
	@AdminRequired
	@Override
	public ResponseEntity<ResponseDto<AdminConsumerProfileResponseDto>> getConsumer(
		@PathVariable("consumerId") Long consumerId) {
		return ResponseDto.success(ResponseType.SUCCESS, adminConsumerService.getConsumerProfileById(consumerId));
	}

	@GetMapping("/consumercount")
	@AdminRequired
	@Override
	public ResponseEntity<ResponseDto<Long>> ConsumerCount() {
		return ResponseDto.success(ResponseType.SUCCESS, adminConsumerService.getCount());
	}

}
