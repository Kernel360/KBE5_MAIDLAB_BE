package kernel.maidlab.admin.consumer;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import kernel.maidlab.api.consumer.dto.response.ConsumerListResponseDto;
import kernel.maidlab.api.consumer.service.ConsumerService;
import kernel.maidlab.common.dto.ResponseDto;
import kernel.maidlab.common.enums.ResponseType;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/admin/consumer")
public class AdminConsumerController implements AdminConsumerApi {

	private final ConsumerService consumerService;

	@GetMapping
	@Override
	public ResponseEntity<ResponseDto<Page<ConsumerListResponseDto>>> getConsumers(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
		Page<ConsumerListResponseDto> response = consumerService.getConsumerBypage(page, size);
		return ResponseDto.success(ResponseType.SUCCESS, response);
	}

}
