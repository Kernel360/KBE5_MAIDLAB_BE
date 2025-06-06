package kernel.maidlab.api.board.controller;

import jakarta.servlet.http.HttpServletRequest;
import kernel.maidlab.api.board.dto.request.BoardUpdateRequestDto;
import kernel.maidlab.api.board.dto.request.ConsumerBoardRequestDto;
import kernel.maidlab.api.board.dto.response.ConsumerBoardDetailResponseDto;
import kernel.maidlab.api.board.dto.response.ConsumerBoardResponseDto;
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
    public ResponseEntity<ResponseDto<String>> createBoard(
            HttpServletRequest request,
            @RequestBody ConsumerBoardRequestDto consumerBoardRequestDto){

        boardService.createConsumerBoard(request, consumerBoardRequestDto);
        return ResponseDto.success("게시글이 성공적으로 등록완료 되었습니다.");
    }

    // 수요자의 작성글 전체 조회
    @GetMapping
    public ResponseEntity<ResponseDto<List<ConsumerBoardResponseDto>>> getConsumerBoardList(HttpServletRequest request){

        List<ConsumerBoardResponseDto> consumerBoardList = boardService.getConsumerBoardList(request);
        return ResponseDto.success(consumerBoardList);
    }

    //  수요자 글 상세 조회
    @GetMapping("/{boardId}")
    public ResponseEntity<ResponseDto<ConsumerBoardDetailResponseDto>> getConsumerBoard(
            HttpServletRequest request,
            @PathVariable("boardId")Long boardId) throws AccessDeniedException {

        ConsumerBoardDetailResponseDto consumerBoardDetailDto = boardService.getConsumerBoard(request, boardId);
        return ResponseDto.success(consumerBoardDetailDto);
    }

    // 게시판 글 수정
    @PatchMapping("/{boardId}")
    public ResponseEntity<ResponseDto<Object>> updateBoard(
            HttpServletRequest request,
            @PathVariable("boardId")Long boardId,
            @RequestBody BoardUpdateRequestDto boardUpdateRequestDto
    ){
        boardService.modifyBoard(request, boardId, boardUpdateRequestDto);
        return ResponseDto.success("게시글 수정이 완료되었습니다.");
    }

    //게시글 삭제
    @DeleteMapping("/{boardId}")
    public ResponseEntity<ResponseDto<Object>> deleteBoard(
            HttpServletRequest request,
            @PathVariable("boardId") Long boardId
    ){
        boardService.deleteBoard(request, boardId);
        return ResponseDto.success("게시글이 삭제되었습니다.");

    }



}
