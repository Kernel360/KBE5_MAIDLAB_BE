package kernel.maidlab.admin.consumer;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import kernel.maidlab.api.consumer.dto.response.ConsumerListResponseDto;
import kernel.maidlab.api.consumer.dto.response.ConsumerProfileResponseDto;
import kernel.maidlab.common.dto.ResponseDto;

@Tag(name = "Consumer", description = "Consumer API")

public interface AdminConsumerApi {
	@Operation(summary = "수요자 계정 조회", description = "계정 조회 API")
	@ApiResponses(value = {@ApiResponse(responseCode = "200", description = "조회 성공 (SU)"),
		@ApiResponse(responseCode = "401", description = "Authorization failed (AF)"),
		@ApiResponse(responseCode = "500", description = "Database error (DBE)")})
	ResponseEntity<ResponseDto<Page<ConsumerListResponseDto>>> getConsumers(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size);

	@GetMapping("/{consumerId}")
	@Operation(summary = "수요자 계정 상세 조회", description = "계정 상세 조회 API")
	@ApiResponses(value = {@ApiResponse(responseCode = "200", description = "조회 성공 (SU)"),
		@ApiResponse(responseCode = "401", description = "Authorization failed (AF)"),
		@ApiResponse(responseCode = "500", description = "Database error (DBE)")})
	ResponseEntity<ConsumerProfileResponseDto> getConsumer(@PathVariable("consumerId") int consumerId);
}
