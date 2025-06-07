package kernel.maidlab.admin.manager;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import kernel.maidlab.api.consumer.dto.response.ConsumerListResponseDto;
import kernel.maidlab.api.consumer.dto.response.ConsumerProfileResponseDto;
import kernel.maidlab.api.manager.dto.ManagerListResponseDto;
import kernel.maidlab.api.manager.dto.ManagerResponseDto;
import kernel.maidlab.common.dto.ResponseDto;

@Tag(name = "Manager", description = "Manager API")

public interface AdminManagerApi {
	@Operation(summary = "매니저 계정 조회", description = "계정 조회 API")
	@ApiResponses(value = {@ApiResponse(responseCode = "200", description = "조회 성공 (SU)"),
		@ApiResponse(responseCode = "401", description = "Authorization failed (AF)"),
		@ApiResponse(responseCode = "500", description = "Database error (DBE)")})
	ResponseEntity<ResponseDto<Page<ManagerListResponseDto>>> getManagers(@RequestParam(defaultValue = "0") int page,
		@RequestParam(defaultValue = "10") int size);

	@Operation(summary = "매니저 계정 상세 조회", description = "계정 상세 조회 API")
	@ApiResponses(value = {@ApiResponse(responseCode = "200", description = "조회 성공 (SU)"),
		@ApiResponse(responseCode = "401", description = "Authorization failed (AF)"),
		@ApiResponse(responseCode = "500", description = "Database error (DBE)")})
	ResponseEntity<ResponseDto<ManagerResponseDto>> getManager(@PathVariable("managerId") Long managerId);

	@PatchMapping("/{managerId}/approve")
	@Operation(summary = "매니저 계정 생성 승인", description = "계정 승인 API")
	@ApiResponses(value = {@ApiResponse(responseCode = "200", description = "계정 승인 완료 (SU)"),
		@ApiResponse(responseCode = "401", description = "Authorization failed (AF)"),
		@ApiResponse(responseCode = "500", description = "Database error (DBE)")})
	ResponseEntity<ResponseDto<String>> approveManager(@PathVariable("managerId") Long managerId);

	@PatchMapping("/{managerId}/reject")
	@Operation(summary = "매니저 계정 생성 거부", description = "계정 거부 API")
	@ApiResponses(value = {@ApiResponse(responseCode = "200", description = "계정 거부 완료 (SU)"),
		@ApiResponse(responseCode = "401", description = "Authorization failed (AF)"),
		@ApiResponse(responseCode = "500", description = "Database error (DBE)")})
	ResponseEntity<ResponseDto<String>> rejectManager(@PathVariable("managerId") Long managerId);
}
