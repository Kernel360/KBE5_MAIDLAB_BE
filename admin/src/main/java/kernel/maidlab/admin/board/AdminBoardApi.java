package kernel.maidlab.admin.board;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpServletRequest;
import kernel.maidlab.api.board.dto.request.AnswerRequestDto;
import kernel.maidlab.api.board.dto.response.ConsumerBoardResponseDto;
import kernel.maidlab.common.dto.ResponseDto;

public interface AdminBoardApi {

	@GetMapping
	ResponseEntity<ResponseDto<List<ConsumerBoardResponseDto>>> refund(HttpServletRequest request,
		@RequestParam int page, @RequestParam int size);

	@GetMapping("/consultation")
	ResponseEntity<ResponseDto<List<ConsumerBoardResponseDto>>> consultation(HttpServletRequest request,
		@RequestParam int page, @RequestParam int size);

	@PostMapping("/answer")
	ResponseEntity<ResponseDto<String>> answer(AnswerRequestDto requestDto, HttpServletRequest request, Long boardId);

	@PatchMapping("/answer/{answerId}")
	ResponseEntity<ResponseDto<String>> answer(AnswerRequestDto requestDto, @PathVariable Long answerId);
}
