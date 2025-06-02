package kernel.maidlab.api.board.service;

import jakarta.servlet.http.HttpServletRequest;
import kernel.maidlab.api.auth.entity.Consumer;
import kernel.maidlab.api.board.dto.request.ConsumerBoardRequestDto;
import kernel.maidlab.api.board.entity.Board;
import kernel.maidlab.api.board.entity.Image;
import kernel.maidlab.api.board.repository.BoardRepository;
import kernel.maidlab.api.board.repository.ImageRepository;
import kernel.maidlab.api.util.AuthUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ConsumerBoardServiceImpl{

    private final BoardRepository boardRepository;
    private final ImageRepository imageRepository;
    private final AuthUtil authUtil;

    // 수요자 게시판 글 생성
    public void createConsumerBoard(HttpServletRequest request,
                                    ConsumerBoardRequestDto consumerBoardRequestDto){

        Consumer consumer = authUtil.getConsumer(request);
        Board board = Board.createConsumerBoard(consumer, consumerBoardRequestDto);
        boardRepository.save(board);

        consumerBoardRequestDto.getImageUrls()
                .forEach((imageUrl) -> imageRepository.save(new Image(board, imageUrl)));
    }


}
