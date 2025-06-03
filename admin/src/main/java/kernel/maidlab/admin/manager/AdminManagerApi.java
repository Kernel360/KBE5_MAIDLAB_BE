package kernel.maidlab.admin.manager;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import kernel.maidlab.api.consumer.dto.response.ConsumerListResponseDto;
import kernel.maidlab.api.consumer.dto.response.ConsumerProfileResponseDto;
import kernel.maidlab.api.manager.dto.ManagerListResponseDto;
import kernel.maidlab.api.manager.dto.ManagerResponseDto;
import kernel.maidlab.common.dto.ResponseDto;

@Tag(name = "Manager", description = "Manager API")

public interface AdminManagerApi {
	@Operation(summary = "매니저 계정 조회", description = "계정 조회 API")
	@ApiResponse(responseCode = "200", description = "계정 조회 완료")
	ResponseEntity<ResponseDto<Page<ManagerListResponseDto>>> getManagers(@RequestParam(defaultValue = "0") int page,
		@RequestParam(defaultValue = "10") int size);


	ResponseEntity<ResponseDto<ManagerResponseDto>> getManager(@PathVariable("managerId") Long managerId);

	@PatchMapping("/{managerId}/Approve")
	ResponseEntity<ResponseDto<String>> approveManager(@PathVariable("managerId") Long managerId);

	@PatchMapping("/{managerId}/reject")
	ResponseEntity<ResponseDto<String>> rejectManager(@PathVariable("managerId") Long managerId);
}
