package kernel.maidlab.board;

import java.nio.file.AccessDeniedException;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import kernel.maidlab.common.dto.ResponseDto;
import kernel.maidlab.common.enums.UserType;
import kernel.maidlab.core.aop.annotation.auth.AuthRequired;
import kernel.maidlab.domain.board.dto.request.BoardRequestDto;
import kernel.maidlab.domain.board.dto.request.BoardUpdateRequestDto;
import kernel.maidlab.domain.board.dto.response.BoardDetailResponseDto;
import kernel.maidlab.domain.board.dto.response.BoardResponseDto;
import kernel.maidlab.domain.board.service.BoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/board")
public class BoardController {

	private final BoardService boardService;

	@PostMapping
	@AuthRequired(roles = {UserType.CONSUMER, UserType.MANAGER})
	public ResponseEntity<ResponseDto<String>> createBoard(
		HttpServletRequest request,
		@RequestBody BoardRequestDto boardRequestDto
	) {
		boardService.createBoard(request, boardRequestDto);
		return ResponseDto.success("게시글이 성공적으로 등록완료 되었습니다.");
	}

	@GetMapping
	@AuthRequired(roles = {UserType.CONSUMER, UserType.MANAGER})
	public ResponseEntity<ResponseDto<List<BoardResponseDto>>> getConsumerBoardList(HttpServletRequest request) {

		List<BoardResponseDto> consumerBoardList = boardService.getConsumerBoardList(request);
		return ResponseDto.success(consumerBoardList);
	}

	@GetMapping("/{boardId}")
	@AuthRequired(roles = {UserType.CONSUMER, UserType.MANAGER})
	public ResponseEntity<ResponseDto<BoardDetailResponseDto>> getConsumerBoard(
		HttpServletRequest request,
		@PathVariable("boardId") Long boardId) throws AccessDeniedException {

		BoardDetailResponseDto consumerBoardDetailDto = boardService.getConsumerBoard(request, boardId);
		return ResponseDto.success(consumerBoardDetailDto);
	}

	@PatchMapping("/{boardId}")
	@AuthRequired(roles = {UserType.CONSUMER, UserType.MANAGER})
	public ResponseEntity<ResponseDto<Object>> updateBoard(
		HttpServletRequest request,
		@PathVariable("boardId") Long boardId,
		@RequestBody BoardUpdateRequestDto boardUpdateRequestDto
	) {
		boardService.modifyBoard(request, boardId, boardUpdateRequestDto);
		return ResponseDto.success("게시글 수정이 완료되었습니다.");
	}

	@DeleteMapping("/{boardId}")
	@AuthRequired(roles = {UserType.CONSUMER, UserType.MANAGER})
	public ResponseEntity<ResponseDto<Object>> deleteBoard(
		HttpServletRequest request,
		@PathVariable("boardId") Long boardId
	) {
		boardService.deleteBoard(request, boardId);
		return ResponseDto.success("게시글이 삭제되었습니다.");

	}

}
