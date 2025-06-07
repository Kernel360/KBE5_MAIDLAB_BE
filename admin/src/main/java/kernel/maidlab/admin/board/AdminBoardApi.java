package kernel.maidlab.admin.board;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletRequest;
import kernel.maidlab.api.board.dto.request.AnswerRequestDto;
import kernel.maidlab.api.board.dto.response.BoardResponseDto;
import kernel.maidlab.common.dto.ResponseDto;

public interface AdminBoardApi {

	@GetMapping("/refund")
	@Operation(summary = "환불 게시판 조회", description = "환불 게시판의 게시물을 조회합니다.")
	@ApiResponses(value = {@ApiResponse(responseCode = "200", description = "게시물 조회 성공 (SU)"),
		@ApiResponse(responseCode = "401", description = "Authorization failed (AF)"),
		@ApiResponse(responseCode = "500", description = "Database error (DBE)")})
	ResponseEntity<ResponseDto<List<BoardResponseDto>>> refund(HttpServletRequest request,
                                                               @RequestParam int page, @RequestParam int size);

	@GetMapping("/consultation")
	@Operation(summary = "상담 게시판 조회", description = "상담 게시판의 게시물을 조회합니다.")
	@ApiResponses(value = {@ApiResponse(responseCode = "200", description = "게시물 조회 성공 (SU)"),
		@ApiResponse(responseCode = "401", description = "Authorization failed (AF)"),
		@ApiResponse(responseCode = "500", description = "Database error (DBE)")})
	ResponseEntity<ResponseDto<List<BoardResponseDto>>> consultation(HttpServletRequest request,
                                                                     @RequestParam int page, @RequestParam int size);
	@PostMapping("/answer")
	@Operation(summary = "답변 게시물 생성", description = "답변 게시판의 게시물을 생성합니다.")
	@ApiResponses(value = {@ApiResponse(responseCode = "200", description = "게시물 생성 성공 (SU)"),
		@ApiResponse(responseCode = "401", description = "Authorization failed (AF)"),
		@ApiResponse(responseCode = "500", description = "Database error (DBE)")})
	ResponseEntity<ResponseDto<String>> answer(AnswerRequestDto requestDto, HttpServletRequest request, Long boardId);

	@PatchMapping("/answer/{answerId}")
	@Operation(summary = "답변 게시물 수정", description = "답변 게시판의 게시물을 수정합니다.")
	@ApiResponses(value = {@ApiResponse(responseCode = "200", description = "게시물 수정 성공 (SU)"),
		@ApiResponse(responseCode = "401", description = "Authorization failed (AF)"),
		@ApiResponse(responseCode = "500", description = "Database error (DBE)")})
	ResponseEntity<ResponseDto<String>> answer(AnswerRequestDto requestDto, @PathVariable Long answerId);

}
