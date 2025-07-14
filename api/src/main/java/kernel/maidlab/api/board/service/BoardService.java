package kernel.maidlab.api.board.service;

import java.nio.file.AccessDeniedException;
import java.util.List;

import jakarta.servlet.http.HttpServletRequest;
import kernel.maidlab.common.dto.board.request.BoardRequestDto;
import kernel.maidlab.common.dto.board.request.BoardUpdateRequestDto;
import kernel.maidlab.common.dto.board.response.BoardDetailResponseDto;
import kernel.maidlab.common.dto.board.response.BoardResponseDto;

public interface BoardService {

	// 수요자 게시판 글 생성
	void createBoard(HttpServletRequest request,
		BoardRequestDto boardRequestDto);

	// 수요자 게시글 전체 조회
	List<BoardResponseDto> getConsumerBoardList(HttpServletRequest request);

	// 수요자 게시글 상세 조회
	BoardDetailResponseDto getConsumerBoard(HttpServletRequest request, Long id) throws AccessDeniedException;

	void modifyBoard(HttpServletRequest request, Long id, BoardUpdateRequestDto boardUpdateRequestDto);

	void deleteBoard(HttpServletRequest request, Long boardId);

}
