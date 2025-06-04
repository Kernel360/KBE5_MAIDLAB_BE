package kernel.maidlab.api.board.controller;

import jakarta.servlet.http.HttpServletRequest;
import kernel.maidlab.api.board.dto.request.ConsumerBoardRequestDto;
import kernel.maidlab.api.board.dto.response.ConsumerBoardDetailResponse;
import kernel.maidlab.api.board.dto.response.ConsumerBoardResponseDto;
import kernel.maidlab.api.board.service.ConsumerBoardService;
import kernel.maidlab.common.dto.ResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/board")
public class ConsumerBoardController {

    private final ConsumerBoardService consumerBoardService;

    // 수요자 게시판 글 생성
    @PostMapping
    public ResponseEntity<ResponseDto<String>> createBoard(HttpServletRequest request,
                                      @RequestBody ConsumerBoardRequestDto consumerBoardRequestDto){
        consumerBoardService.createConsumerBoard(request, consumerBoardRequestDto);
        return ResponseDto.success("게시글이 성공적으로 등록완료 되었습니다.");
    }

    // 수요자의 작성글 전체 조회
    @GetMapping
    public ResponseEntity<ResponseDto<List<ConsumerBoardResponseDto>>> getConsumerBoardList(HttpServletRequest request){

        List<ConsumerBoardResponseDto> consumerBoardList = consumerBoardService.getConsumerBoardList(request);
        return ResponseDto.success(consumerBoardList);
    }

    //  수요자 글 상세 조회
    @GetMapping("/{boardId}")
    public ResponseEntity<ResponseDto<ConsumerBoardDetailResponse>> getConsumerBoard(
            HttpServletRequest request,
            @PathVariable("boardId")Long boardId) throws AccessDeniedException {

        ConsumerBoardDetailResponse consumerBoardDetailDto = consumerBoardService.getConsumerBoard(request, boardId);
        return ResponseDto.success(consumerBoardDetailDto);
    }


    // 답변 생성

    // 답변 수정


}
