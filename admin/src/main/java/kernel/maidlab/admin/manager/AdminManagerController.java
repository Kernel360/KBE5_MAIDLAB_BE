package kernel.maidlab.admin.manager;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import kernel.maidlab.api.manager.dto.ManagerListResponseDto;
import kernel.maidlab.api.manager.service.ManagerService;
import kernel.maidlab.common.dto.ResponseDto;
import kernel.maidlab.common.enums.ResponseType;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api/admin/manager")
@RequiredArgsConstructor
public class AdminManagerController implements AdminManagerApi{

	private final ManagerService managerService;

	@Override
	public ResponseEntity<ResponseDto<Page<ManagerListResponseDto>>> getManagers(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
		Page<ManagerListResponseDto> response = managerService.getManagerBypage(page, size);
		return ResponseDto.success(ResponseType.SUCCESS, response);
	}
}
