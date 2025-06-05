package kernel.maidlab.api.board.service;

import jakarta.servlet.http.HttpServletRequest;
import kernel.maidlab.api.board.dto.request.AnswerRequestDto;
import kernel.maidlab.api.board.dto.request.ConsumerBoardRequestDto;
import kernel.maidlab.api.board.dto.response.ConsumerBoardDetailResponseDto;
import kernel.maidlab.api.board.dto.response.ConsumerBoardResponseDto;

import java.nio.file.AccessDeniedException;
import java.util.List;

public interface ConsumerBoardService {

    // 수요자 게시판 글 생성
    void createConsumerBoard(HttpServletRequest request,
                             ConsumerBoardRequestDto consumerBoardRequestDto);

    // 수요자 게시글 전체 조회
    List<ConsumerBoardResponseDto>  getConsumerBoardList(HttpServletRequest request);

    // 수요자 게시글 상세 조회
    ConsumerBoardDetailResponseDto getConsumerBoard(HttpServletRequest request, Long id) throws AccessDeniedException;

    List<ConsumerBoardResponseDto> getAllRefundBoardList(HttpServletRequest request, int page, int size);

    List<ConsumerBoardResponseDto> getAllConsultationBoardList(HttpServletRequest request, int page, int size);

    void createAnswer(AnswerRequestDto requestDto, HttpServletRequest request, Long boardId);

    void modifyAnswer(AnswerRequestDto requestDto, Long answerId);
}
