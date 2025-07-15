package kernel.maidlab.admin.matching.controller;

import jakarta.servlet.http.HttpServletRequest;
import kernel.maidlab.admin.matching.service.AdminMatchingService;
import kernel.maidlab.api.matching.dto.response.MatchingResponseDto;
import kernel.maidlab.common.dto.ResponseDto;
import kernel.maidlab.common.enums.ResponseType;
import kernel.maidlab.common.enums.Status;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api/admin/matching")
@RestController
public class AdminMatchingController implements AdminMatchingApi {

	private final AdminMatchingService adminmatchingService;

	@GetMapping
	@Override
	public ResponseEntity<ResponseDto<List<MatchingResponseDto>>> allMatching(HttpServletRequest request,
																			  @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
		List<MatchingResponseDto> response = adminmatchingService.allMatching(request, page, size);
		return ResponseDto.success(ResponseType.SUCCESS, response);
	}

	@GetMapping("/status")
	@Override
	public ResponseEntity<ResponseDto<List<MatchingResponseDto>>> statusMatching(Status status,
		HttpServletRequest request, @RequestParam(defaultValue = "0") int page,
		@RequestParam(defaultValue = "10") int size) {
		List<MatchingResponseDto> response = adminmatchingService.statusMatching(status, page, size);
		return ResponseDto.success(ResponseType.SUCCESS, response);
	}

	@PatchMapping("/managerchange")
	@Override
	public ResponseEntity<ResponseDto<String>> managerChange(@RequestParam Long reservationId,
		@RequestParam Long managerId) {
		adminmatchingService.changeManager(reservationId, managerId);
		return ResponseDto.success(ResponseType.SUCCESS, "변경 완료");
	}
}
