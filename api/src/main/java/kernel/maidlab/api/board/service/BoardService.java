package kernel.maidlab.api.board.service;

import jakarta.servlet.http.HttpServletRequest;
import kernel.maidlab.api.board.dto.request.BoardUpdateRequestDto;
import kernel.maidlab.api.board.dto.request.ConsumerBoardRequestDto;
import kernel.maidlab.api.board.dto.response.ConsumerBoardDetailResponseDto;
import kernel.maidlab.api.board.dto.response.ConsumerBoardResponseDto;

import java.nio.file.AccessDeniedException;
import java.util.List;

public interface BoardService {

        void createConsumerBoard(HttpServletRequest request,
                             ConsumerBoardRequestDto consumerBoardRequestDto);

    List<ConsumerBoardResponseDto>  getConsumerBoardList(HttpServletRequest request);

    ConsumerBoardDetailResponseDto getConsumerBoard(HttpServletRequest request, Long id) throws AccessDeniedException;

    void modifyBoard(HttpServletRequest request, Long id, BoardUpdateRequestDto boardUpdateRequestDto);

    void deleteBoard(HttpServletRequest request, Long boardId);

}
