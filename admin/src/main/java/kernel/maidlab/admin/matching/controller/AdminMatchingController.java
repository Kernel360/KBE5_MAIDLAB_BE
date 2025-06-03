package kernel.maidlab.admin.matching.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import kernel.maidlab.api.manager.repository.ManagerRepository;
import kernel.maidlab.api.matching.dto.response.MatchingResponseDto;
import kernel.maidlab.api.matching.service.MatchingService;
import kernel.maidlab.common.dto.ResponseDto;
import kernel.maidlab.common.enums.ResponseType;
import kernel.maidlab.common.enums.Status;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RequestMapping("/api/admin/matching")
@RestController
public class AdminMatchingController implements AdminMatchingApi {

	private final MatchingService matchingService;
	private final ManagerRepository managerRepository;

	@GetMapping
	@Override
	public ResponseEntity<ResponseDto<List<MatchingResponseDto>>> allMatching(HttpServletRequest request, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
		List<MatchingResponseDto> response = matchingService.allMatching(request, page, size);
		return ResponseDto.success(ResponseType.SUCCESS, response);
	}

	@GetMapping("/status")
	@Override
	public ResponseEntity<ResponseDto<List<MatchingResponseDto>>> statusMatching(Status status,
		HttpServletRequest request , @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
		List<MatchingResponseDto> response = matchingService.statusMatching(status, page, size);
		return ResponseDto.success(ResponseType.SUCCESS, response);
	}

	@PatchMapping("/managerchange")
	@Override
	public ResponseEntity<ResponseDto<String>> managerChange(@RequestParam Long reservationId,
		@RequestParam Long managerId) {
		matchingService.changeManager(reservationId, managerId);
		return ResponseDto.success(ResponseType.SUCCESS, "변경 완료");
	}

}
