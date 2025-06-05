package kernel.maidlab.admin.board;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import kernel.maidlab.api.board.dto.request.AnswerRequestDto;
import kernel.maidlab.api.board.dto.response.ConsumerBoardResponseDto;
import kernel.maidlab.api.board.service.ConsumerBoardService;
import kernel.maidlab.common.dto.ResponseDto;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/admin/board")
@RequiredArgsConstructor
public class AdminBoardControllerImpl implements AdminBoardApi {


	private final ConsumerBoardService boardService;

	@GetMapping
	@Override
	public ResponseEntity<ResponseDto<List<ConsumerBoardResponseDto>>> refund(HttpServletRequest request,
		@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
		List<ConsumerBoardResponseDto> dto = boardService.getAllRefundBoardList(request, page, size);
		return ResponseDto.success(dto);
	}

	@GetMapping("/consultation")
	@Override
	public ResponseEntity<ResponseDto<List<ConsumerBoardResponseDto>>> consultation(HttpServletRequest request,
		@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
		List<ConsumerBoardResponseDto> dto = boardService.getAllConsultationBoardList(request, page, size);
		return ResponseDto.success(dto);
	}

	@PostMapping("/answer")
	@Override
	public ResponseEntity<ResponseDto<String>> answer(AnswerRequestDto requestDto, HttpServletRequest request,
		Long boardId) {
		boardService.createAnswer(requestDto, request, boardId);
		return ResponseDto.success("답변 생성 완료");
	}

	@PatchMapping("/answer/{answerId}")
	@Override
	public ResponseEntity<ResponseDto<String>> answer(AnswerRequestDto requestDto, @PathVariable Long answerId) {
		boardService.modifyAnswer(requestDto, answerId);
		return ResponseDto.success("답변 수정 완료");
	}
}
