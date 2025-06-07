package kernel.maidlab.api.board.controller;

import jakarta.servlet.http.HttpServletRequest;
import kernel.maidlab.api.board.dto.request.BoardRequestDto;
import kernel.maidlab.api.board.dto.response.BoardDetailResponseDto;
import kernel.maidlab.api.board.dto.response.BoardResponseDto;
import kernel.maidlab.api.board.service.BoardService;
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
public class BoardController {

    private final BoardService boardService;

    // 수요자 게시판 글 생성
    @PostMapping
    public ResponseEntity<ResponseDto<String>> createBoard(HttpServletRequest request,
                                      @RequestBody BoardRequestDto boardRequestDto){
        boardService.createConsumerBoard(request, boardRequestDto);
        return ResponseDto.success("게시글이 성공적으로 등록완료 되었습니다.");
    }

    // 수요자의 작성글 전체 조회
    @GetMapping
    public ResponseEntity<ResponseDto<List<BoardResponseDto>>> getConsumerBoardList(HttpServletRequest request){

        List<BoardResponseDto> consumerBoardList = boardService.getConsumerBoardList(request);
        return ResponseDto.success(consumerBoardList);
    }

    //  수요자 글 상세 조회
    @GetMapping("/{boardId}")
    public ResponseEntity<ResponseDto<BoardDetailResponseDto>> getConsumerBoard(
            HttpServletRequest request,
            @PathVariable("boardId")Long boardId) throws AccessDeniedException {

        BoardDetailResponseDto consumerBoardDetailDto = boardService.getConsumerBoard(request, boardId);
        return ResponseDto.success(consumerBoardDetailDto);
    }


    // 답변 생성

    // 답변 수정


}
