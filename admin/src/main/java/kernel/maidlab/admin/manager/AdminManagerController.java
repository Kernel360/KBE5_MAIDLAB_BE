package kernel.maidlab.admin.manager;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import kernel.maidlab.api.manager.dto.ManagerListResponseDto;
import kernel.maidlab.api.manager.dto.ManagerResponseDto;
import kernel.maidlab.api.manager.service.ManagerService;
import kernel.maidlab.common.dto.ResponseDto;
import kernel.maidlab.common.enums.ResponseType;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api/admin/manager")
@RequiredArgsConstructor
public class AdminManagerController implements AdminManagerApi{

	private final ManagerService managerService;


	@GetMapping
	@Override
	public ResponseEntity<ResponseDto<Page<ManagerListResponseDto>>> getManagers(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
		Page<ManagerListResponseDto> response = managerService.getManagerBypage(page, size);
		return ResponseDto.success(ResponseType.SUCCESS, response);

	}

	@GetMapping("/{managerId}")
	@Override
	public ResponseEntity<ResponseDto<ManagerResponseDto>> getManager(@PathVariable("managerId") Long managerId) {
		ManagerResponseDto response = managerService.getManager(managerId);
		return ResponseDto.success(response);
	}

	@PatchMapping("/{managerId}/approve")
	@Override
	public ResponseEntity<ResponseDto<String>> approveManager(@PathVariable("managerId") Long managerId){
		managerService.approveManager(managerId);
		return ResponseDto.success("매니저 승인 완료");
	}

	@PatchMapping("/{managerId}/reject")
	@Override
	public ResponseEntity<ResponseDto<String>> rejectManager(@PathVariable("managerId") Long managerId){
		managerService.rejectManager(managerId);
		return ResponseDto.success("매니저 거절 완료");
	}

}
