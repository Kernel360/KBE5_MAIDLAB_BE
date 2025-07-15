package kernel.maidlab.admin.manager.controller;

import jakarta.servlet.http.HttpServletRequest;
import kernel.maidlab.admin.manager.service.AdminManagerService;
import kernel.maidlab.api.manager.dto.ManagerListResponseDto;
import kernel.maidlab.api.manager.dto.response.AdminManagerResponseDto;
import kernel.maidlab.common.dto.ResponseDto;
import kernel.maidlab.common.enums.ResponseType;
import kernel.maidlab.common.enums.Status;
import kernel.maidlab.core.aop.annotation.auth.AdminRequired;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/admin/manager")
@RequiredArgsConstructor
public class AdminManagerController implements AdminManagerApi {

	private final AdminManagerService adminManagerService;

	@GetMapping
	@Override
	@AdminRequired
	public ResponseEntity<ResponseDto<Page<ManagerListResponseDto>>> getManagers(
		@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
		Page<ManagerListResponseDto> response = adminManagerService.getManagerBypage(page, size);
		return ResponseDto.success(ResponseType.SUCCESS, response);

	}

	@GetMapping("/status")
	@Override
	@AdminRequired
	public ResponseEntity<ResponseDto<Page<ManagerListResponseDto>>> getManagers(
		@RequestParam(defaultValue = "0") int page,
		@RequestParam(defaultValue = "10") int size,
		@RequestParam Status status,
		@RequestParam(defaultValue = "false") boolean sortByRating,
		@RequestParam(required = false) Boolean isDescending) {

		Page<ManagerListResponseDto> response = adminManagerService.getManagerByPageWithStatus(page, size, status,
			sortByRating,
			isDescending);
		return ResponseDto.success(ResponseType.SUCCESS, response);
	}

	@GetMapping("/{managerId}")
	@Override
	@AdminRequired
	public ResponseEntity<ResponseDto<AdminManagerResponseDto>> getManager(@PathVariable("managerId") Long managerId) {
		AdminManagerResponseDto response = adminManagerService.getManager(managerId);
		return ResponseDto.success(response);
	}

	@PatchMapping("/{managerId}/approve")
	@Override
	@AdminRequired
	public ResponseEntity<ResponseDto<String>> approveManager(@PathVariable("managerId") Long managerId) {
		adminManagerService.approveManager(managerId);
		return ResponseDto.success("매니저 승인 완료");
	}

	@PatchMapping("/{managerId}/reject")
	@Override
	@AdminRequired
	public ResponseEntity<ResponseDto<String>> rejectManager(@PathVariable("managerId") Long managerId) {
		adminManagerService.rejectManager(managerId);
		return ResponseDto.success("매니저 거절 완료");
	}

	@GetMapping("/managercount")
	@Override
	@AdminRequired
	public ResponseEntity<ResponseDto<Long>> managerCount(HttpServletRequest request) {
		return ResponseDto.success(adminManagerService.managerCount(request));
	}

	@GetMapping("/newmanagercount")
	@Override
	@AdminRequired
	public ResponseEntity<ResponseDto<Long>> newManagerCount(HttpServletRequest request) {
		return ResponseDto.success(adminManagerService.newManagerCount(request));
	}

}
