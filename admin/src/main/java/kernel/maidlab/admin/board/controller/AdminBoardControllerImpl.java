package kernel.maidlab.admin.board.controller;

import jakarta.servlet.http.HttpServletRequest;
import kernel.maidlab.admin.board.service.AdminBoardServiceImpl;
import kernel.maidlab.domain.board.dto.request.AnswerRequestDto;
import kernel.maidlab.domain.board.dto.response.AdminBoardDetailResponseDto;
import kernel.maidlab.domain.board.dto.response.AdminBoardResponseDto;
import kernel.maidlab.common.dto.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.List;

@RestController
@RequestMapping("/api/admin/board")
@RequiredArgsConstructor
public class AdminBoardControllerImpl implements AdminBoardApi {

	private final AdminBoardServiceImpl adminBoardService;

	@GetMapping("/refund")
	@Override
	public ResponseEntity<ResponseDto<List<AdminBoardResponseDto>>> refund(HttpServletRequest request,
																		   @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
		return adminBoardService.getAllRefundBoardList(request, page, size);
	}

	@GetMapping("/consultation")
	@Override
	public ResponseEntity<ResponseDto<List<AdminBoardResponseDto>>> consultation(HttpServletRequest request,
		@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
		return adminBoardService.getAllConsultationBoardList(request, page, size);

	}

	@GetMapping("/{boardId}")
	@Override
	public ResponseEntity<ResponseDto<AdminBoardDetailResponseDto>> detail(HttpServletRequest request,
																		   @PathVariable Long boardId) throws AccessDeniedException {
		return adminBoardService.adminGetConsumerBoard(request, boardId);
	}

	@PostMapping("/answer/{boardId}")
	@Override
	public ResponseEntity<ResponseDto<Void>> answer(@RequestBody AnswerRequestDto requestDto,
		HttpServletRequest request,
		@PathVariable Long boardId) {
		return adminBoardService.createAnswer(requestDto, request, boardId);
	}

	@PatchMapping("/answer/{answerId}")
	@Override
	public ResponseEntity<ResponseDto<Void>> answer(@RequestBody AnswerRequestDto requestDto,
		@PathVariable Long answerId) {
		return adminBoardService.modifyAnswer(requestDto, answerId);
	}

	@GetMapping("/refundboardcount")
	@Override
	public ResponseEntity<ResponseDto<Long>> refundBoardCount(HttpServletRequest request) {
		return ResponseDto.success(adminBoardService.getRefundBoardWithoutAnswerCount(request));
	}

	@GetMapping("/counselboardcount")
	@Override
	public ResponseEntity<ResponseDto<Long>> counselBoardCount(HttpServletRequest request) {
		return ResponseDto.success(adminBoardService.getCounselBoardWithoutAnswerCount(request));
	}
}
