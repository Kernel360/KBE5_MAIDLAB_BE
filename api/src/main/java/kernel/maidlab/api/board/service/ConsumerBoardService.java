package kernel.maidlab.api.board.service;

import jakarta.servlet.http.HttpServletRequest;
import kernel.maidlab.api.board.dto.request.ConsumerBoardRequestDto;
import kernel.maidlab.api.board.dto.response.ConsumerBoardResponseDto;

import java.util.List;

public interface ConsumerBoardService {

    // 수요자 게시판 글 생성
    void createConsumerBoard(HttpServletRequest request,
                             ConsumerBoardRequestDto consumerBoardRequestDto);

    // 수요자 게시글 전체 조회
    List<ConsumerBoardResponseDto>  getConsumerBoardList(HttpServletRequest request);

}
