package kernel.maidlab.admin.manager.controller;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import kernel.maidlab.admin.manager.service.AdminManagerService;
import kernel.maidlab.common.dto.manager.ManagerListResponseDto;
import kernel.maidlab.common.dto.manager.ManagerResponseDto;
import kernel.maidlab.common.dto.ResponseDto;
import kernel.maidlab.common.enums.ResponseType;
import kernel.maidlab.common.enums.Status;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api/admin/manager")
@RequiredArgsConstructor
public class AdminManagerController implements AdminManagerApi {

	private final AdminManagerService adminManagerService;

	@GetMapping
	@Override
	public ResponseEntity<ResponseDto<Page<ManagerListResponseDto>>> getManagers(
		@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
		Page<ManagerListResponseDto> response = adminManagerService.getManagerBypage(page, size);
		return ResponseDto.success(ResponseType.SUCCESS, response);

	}

	@GetMapping("/status")
	@Override
	public ResponseEntity<ResponseDto<Page<ManagerListResponseDto>>> getManagers(
		@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size,
		@RequestParam Status status) {
		Page<ManagerListResponseDto> response = adminManagerService.getManagerByPageWithStatus(page, size, status);
		return ResponseDto.success(ResponseType.SUCCESS, response);

	}

	@GetMapping("/{managerId}")
	@Override
	public ResponseEntity<ResponseDto<ManagerResponseDto>> getManager(@PathVariable("managerId") Long managerId) {
		ManagerResponseDto response = adminManagerService.getManager(managerId);
		return ResponseDto.success(response);
	}

	@PatchMapping("/{managerId}/approve")
	@Override
	public ResponseEntity<ResponseDto<String>> approveManager(@PathVariable("managerId") Long managerId) {
		adminManagerService.approveManager(managerId);
		return ResponseDto.success("매니저 승인 완료");
	}

	@PatchMapping("/{managerId}/reject")
	@Override
	public ResponseEntity<ResponseDto<String>> rejectManager(@PathVariable("managerId") Long managerId) {
		adminManagerService.rejectManager(managerId);
		return ResponseDto.success("매니저 거절 완료");
	}

	@GetMapping("/managercount")
	@Override
	public ResponseEntity<ResponseDto<Long>> managerCount(HttpServletRequest request) {
		return ResponseDto.success(adminManagerService.managerCount(request));
	}

}
