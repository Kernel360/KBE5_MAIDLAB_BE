package kernel.maidlab.domain.board.service;

import jakarta.servlet.http.HttpServletRequest;
import kernel.maidlab.domain.board.dto.request.BoardRequestDto;
import kernel.maidlab.domain.board.dto.request.BoardUpdateRequestDto;
import kernel.maidlab.domain.board.dto.response.BoardDetailResponseDto;
import kernel.maidlab.domain.board.dto.response.BoardResponseDto;

import java.nio.file.AccessDeniedException;
import java.util.List;

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
