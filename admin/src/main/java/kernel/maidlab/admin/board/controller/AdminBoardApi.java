package kernel.maidlab.admin.board.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletRequest;
import kernel.maidlab.api.board.dto.request.AnswerRequestDto;
import kernel.maidlab.api.board.dto.response.AdminBoardDetailResponseDto;
import kernel.maidlab.api.board.dto.response.AdminBoardResponseDto;
import kernel.maidlab.common.dto.ResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.List;


public interface AdminBoardApi {

	@GetMapping("/refund")
	@Operation(summary = "환불 게시판 조회", description = "환불 게시판의 게시물을 조회합니다.")
	@ApiResponses(value = {@ApiResponse(responseCode = "200", description = "게시물 조회 성공 (SU)"),
		@ApiResponse(responseCode = "401", description = "Authorization failed (AF)"),
		@ApiResponse(responseCode = "500", description = "Database error (DBE)")})
	ResponseEntity<ResponseDto<List<AdminBoardResponseDto>>> refund(HttpServletRequest request,
																	@RequestParam int page, @RequestParam int size);

	@GetMapping("/consultation")
	@Operation(summary = "상담 게시판 조회", description = "상담 게시판의 게시물을 조회합니다.")
	@ApiResponses(value = {@ApiResponse(responseCode = "200", description = "게시물 조회 성공 (SU)"),
		@ApiResponse(responseCode = "401", description = "Authorization failed (AF)"),
		@ApiResponse(responseCode = "500", description = "Database error (DBE)")})
	ResponseEntity<ResponseDto<List<AdminBoardResponseDto>>> consultation(HttpServletRequest request,
		@RequestParam int page, @RequestParam int size);

	@GetMapping("/{boardId}")
	@Operation(summary = "게시판 상세 조회", description = "게시판의 게시물을 상세 조회합니다. 같은 테이블을 사용하기에 id만 있으면 조회 가능합니다.")
	@ApiResponses(value = {@ApiResponse(responseCode = "200", description = "게시물 조회 성공 (SU)"),
		@ApiResponse(responseCode = "401", description = "Authorization failed (AF)"),
		@ApiResponse(responseCode = "500", description = "Database error (DBE)")})
	ResponseEntity<ResponseDto<AdminBoardDetailResponseDto>> detail(HttpServletRequest request,
																	@PathVariable Long boardId) throws AccessDeniedException;

	@PostMapping("/answer")
	@Operation(summary = "답변 게시물 생성", description = "답변 게시판의 게시물을 생성합니다.")
	@ApiResponses(value = {@ApiResponse(responseCode = "200", description = "게시물 생성 성공 (SU)"),
		@ApiResponse(responseCode = "400", description = "Validation failed (VF)"),
		@ApiResponse(responseCode = "401", description = "Authorization failed (AF)"),
		@ApiResponse(responseCode = "500", description = "Database error (DBE)")})
	ResponseEntity<ResponseDto<Void>> answer(@RequestBody AnswerRequestDto requestDto, HttpServletRequest request,
											 @PathVariable Long boardId);

	@PatchMapping("/answer/{answerId}")
	@Operation(summary = "답변 게시물 수정", description = "답변 게시판의 게시물을 수정합니다.")
	@ApiResponses(value = {@ApiResponse(responseCode = "200", description = "게시물 수정 성공 (SU)"),
		@ApiResponse(responseCode = "400", description = "Validation failed (VF)"),
		@ApiResponse(responseCode = "401", description = "Authorization failed (AF)"),
		@ApiResponse(responseCode = "500", description = "Database error (DBE)")})
	ResponseEntity<ResponseDto<Void>> answer(@RequestBody AnswerRequestDto requestDto, @PathVariable Long answerId);

	@GetMapping("/refundboardcount")
	@Operation(summary = "답변 없는 환불 게시판 수 조회", description = "답변이 없는 환불 게시판(소비자)의 게시물 수를 조회합니다.")
	@ApiResponses(value = {@ApiResponse(responseCode = "200", description = "게시물 수 조회 성공 (SU)"),
		@ApiResponse(responseCode = "401", description = "Authorization failed (AF)"),
		@ApiResponse(responseCode = "500", description = "Database error (DBE)")})
	ResponseEntity<ResponseDto<Long>> refundBoardCount(HttpServletRequest request);

	@GetMapping("/counselboardcount")
	@Operation(summary = "답변 없는 상담 게시판 수 조회", description = "답변이 없는 상담 게시판(매니저)의 게시물 수를 조회합니다.")
	@ApiResponses(value = {@ApiResponse(responseCode = "200", description = "게시물 수 조회 성공 (SU)"),
		@ApiResponse(responseCode = "401", description = "Authorization failed (AF)"),
		@ApiResponse(responseCode = "500", description = "Database error (DBE)")})
	ResponseEntity<ResponseDto<Long>> counselBoardCount(HttpServletRequest request);
}
