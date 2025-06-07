package kernel.maidlab.api.board.controller;

import jakarta.servlet.http.HttpServletRequest;


import kernel.maidlab.api.board.dto.request.BoardRequestDto;
import kernel.maidlab.api.board.dto.request.BoardUpdateRequestDto;
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

    @PostMapping
    public ResponseEntity<ResponseDto<String>> createBoard(
            HttpServletRequest request,
            @RequestBody BoardRequestDto boardRequestDto){
        boardService.createConsumerBoard(request, boardRequestDto);
        return ResponseDto.success("게시글이 성공적으로 등록완료 되었습니다.");
    }

    @GetMapping
    public ResponseEntity<ResponseDto<List<BoardResponseDto>>> getConsumerBoardList(HttpServletRequest request){

        List<BoardResponseDto> consumerBoardList = boardService.getConsumerBoardList(request);
        return ResponseDto.success(consumerBoardList);
    }

    @GetMapping("/{boardId}")
    public ResponseEntity<ResponseDto<BoardDetailResponseDto>> getConsumerBoard(
            HttpServletRequest request,
            @PathVariable("boardId")Long boardId) throws AccessDeniedException {

        BoardDetailResponseDto consumerBoardDetailDto = boardService.getConsumerBoard(request, boardId);
        return ResponseDto.success(consumerBoardDetailDto);
    }

    @PatchMapping("/{boardId}")
    public ResponseEntity<ResponseDto<Object>> updateBoard(
            HttpServletRequest request,
            @PathVariable("boardId")Long boardId,
            @RequestBody BoardUpdateRequestDto boardUpdateRequestDto
    ){
        boardService.modifyBoard(request, boardId, boardUpdateRequestDto);
        return ResponseDto.success("게시글 수정이 완료되었습니다.");
    }

    @DeleteMapping("/{boardId}")
    public ResponseEntity<ResponseDto<Object>> deleteBoard(
            HttpServletRequest request,
            @PathVariable("boardId") Long boardId
    ){
        boardService.deleteBoard(request, boardId);
        return ResponseDto.success("게시글이 삭제되었습니다.");

    }

}
