package kernel.maidlab.api.board.service;

import jakarta.servlet.http.HttpServletRequest;
import kernel.maidlab.api.board.dto.request.BoardUpdateRequestDto;
import kernel.maidlab.api.board.dto.request.BoardRequestDto;
import kernel.maidlab.api.board.dto.response.BoardDetailResponseDto;
import kernel.maidlab.api.board.dto.response.BoardResponseDto;

import java.nio.file.AccessDeniedException;
import java.util.List;

public interface BoardService {

        void createConsumerBoard(HttpServletRequest request,
                             BoardRequestDto boardRequestDto);

    List<BoardResponseDto>  getConsumerBoardList(HttpServletRequest request);

    BoardDetailResponseDto getConsumerBoard(HttpServletRequest request, Long id) throws AccessDeniedException;

    void modifyBoard(HttpServletRequest request, Long id, BoardUpdateRequestDto boardUpdateRequestDto);

    void deleteBoard(HttpServletRequest request, Long boardId);

}
