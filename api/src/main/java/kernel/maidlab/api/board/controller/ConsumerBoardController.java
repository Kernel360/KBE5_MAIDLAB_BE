package kernel.maidlab.api.board.controller;

import jakarta.servlet.http.HttpServletRequest;
import kernel.maidlab.api.board.dto.request.ConsumerBoardRequestDto;
import kernel.maidlab.api.board.service.ConsumerBoardServiceImpl;
import kernel.maidlab.common.dto.ResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/consumers/board")
public class ConsumerBoardController {

    private final ConsumerBoardServiceImpl consumerBoardService;

    // 수요자 게시판 글 생성
    @PostMapping
    public ResponseEntity<ResponseDto<Object>> createBoard(HttpServletRequest request,
                                      @RequestBody ConsumerBoardRequestDto consumerBoardRequestDto){
        consumerBoardService.createConsumerBoard(request, consumerBoardRequestDto);
        return ResponseDto.success("게시글이 성공적으로 등록완료 되었습니다.");
    }
}
