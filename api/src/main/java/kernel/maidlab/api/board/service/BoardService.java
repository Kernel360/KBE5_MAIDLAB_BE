package kernel.maidlab.api.board.service;

import jakarta.servlet.http.HttpServletRequest;
import kernel.maidlab.api.board.dto.request.AnswerRequestDto;
import kernel.maidlab.api.board.dto.request.BoardRequestDto;
import kernel.maidlab.api.board.dto.request.BoardUpdateRequestDto;
import kernel.maidlab.api.board.dto.response.BoardDetailResponseDto;
import kernel.maidlab.api.board.dto.response.BoardResponseDto;

import java.nio.file.AccessDeniedException;
import java.util.List;

public interface BoardService {

    // 수요자 게시판 글 생성
    void createConsumerBoard(HttpServletRequest request,
                             BoardRequestDto boardRequestDto);

    // 수요자 게시글 전체 조회
    List<BoardResponseDto>  getConsumerBoardList(HttpServletRequest request);

    // 수요자 게시글 상세 조회
    BoardDetailResponseDto getConsumerBoard(HttpServletRequest request, Long id) throws AccessDeniedException;

    List<BoardResponseDto> getAllRefundBoardList(HttpServletRequest request, int page, int size);

    List<BoardResponseDto> getAllConsultationBoardList(HttpServletRequest request, int page, int size);

    void modifyBoard(HttpServletRequest request, Long id, BoardUpdateRequestDto boardUpdateRequestDto);

    void deleteBoard(HttpServletRequest request, Long boardId);

    void createAnswer(AnswerRequestDto requestDto, HttpServletRequest request, Long boardId);

    void modifyAnswer(AnswerRequestDto requestDto, Long answerId);
}
