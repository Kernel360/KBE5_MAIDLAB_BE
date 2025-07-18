package kernel.maidlab.admin.consumer.controller;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import kernel.maidlab.admin.consumer.service.AdminConsumerService;
import kernel.maidlab.common.dto.ResponseDto;
import kernel.maidlab.common.enums.ResponseType;
import kernel.maidlab.core.aop.annotation.auth.AdminRequired;
import kernel.maidlab.domain.consumer.dto.response.AdminConsumerProfileResponseDto;
import kernel.maidlab.domain.consumer.dto.response.ConsumerListResponseDto;
import lombok.RequiredArgsConstructor;

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

	@GetMapping("/filter")
	@AdminRequired
	@Override
	public ResponseEntity<ResponseDto<Page<ConsumerListResponseDto>>> getConsumersByFilter(
		@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size, @RequestParam Boolean isDeleted) {
		return ResponseDto.success(ResponseType.SUCCESS, adminConsumerService.getConsumerBypageWithFilter(page, size, isDeleted));
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

	@DeleteMapping("/{consumerId}")
	@AdminRequired
	@Override
	public ResponseEntity<ResponseDto<String>> deleteConsumer(@PathVariable("consumerId") Long consumerId) {
		adminConsumerService.deleteConsumer(consumerId);
		return ResponseDto.success(ResponseType.SUCCESS, "수요자 계정이 삭제되었습니다.");
	}

}
