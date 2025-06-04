package kernel.maidlab.api.board.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import kernel.maidlab.api.auth.entity.Consumer;
import kernel.maidlab.api.board.dto.BoardQueryDto;
import kernel.maidlab.api.board.dto.request.ConsumerBoardRequestDto;
import kernel.maidlab.api.board.dto.response.ConsumerBoardDetailResponse;
import kernel.maidlab.api.board.dto.response.ConsumerBoardResponseDto;
import kernel.maidlab.api.board.entity.Board;
import kernel.maidlab.api.board.entity.Image;
import kernel.maidlab.api.board.repository.BoardRepository;
import kernel.maidlab.api.board.repository.ImageRepository;
import kernel.maidlab.api.util.AuthUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.AccessDeniedException;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ConsumerBoardServiceImpl implements ConsumerBoardService{

    private final BoardRepository boardRepository;
    private final ImageRepository imageRepository;
    private final AuthUtil authUtil;

    // 수요자 게시판 글 생성
    public void createConsumerBoard(HttpServletRequest request,
                                    ConsumerBoardRequestDto consumerBoardRequestDto){

        Consumer consumer = authUtil.getConsumer(request);
        Board board = Board.createConsumerBoard(consumer, consumerBoardRequestDto);
        boardRepository.save(board);

        consumerBoardRequestDto.getImages()
                .forEach((imageDto) -> imageRepository.save(
                        new Image(
                                board,
                                imageDto.getImagePath(),
                                imageDto.getName())));
    }

    // 수요자 게시글 전체 조회
    @Transactional(readOnly = true)
    public List<ConsumerBoardResponseDto> getConsumerBoardList(HttpServletRequest request) {

        String uuid = authUtil.getUuid(request);
        List<BoardQueryDto> boardQueryDto = boardRepository.findAllByConsumerUuid(uuid);

        return boardQueryDto.stream()
                .map(ConsumerBoardResponseDto::from)
                .toList();
    }

    // 수요자 글 상세 조회
    public ConsumerBoardDetailResponse getConsumerBoard(HttpServletRequest request,
                                 Long boardId) throws AccessDeniedException {

        // 검증 로직
        Consumer consumer = authUtil.getConsumer(request);
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 게시물 입니다."));

        // 토큰으로 찾은 수요자id와 PathVariable로 넘어온 게시판id로 찾은 consumerId와 비교
        if (!consumer.getId().equals(board.getConsumer().getId())){
            throw new AccessDeniedException("해당 게시글에 접근할 권한이 없습니다.");
        }

        // 답변여부가 true면 답변까지 조회
        if (board.isAnswered()) {
            board = boardRepository.findBoardWithAnswerIfAnswered(boardId)
                    .orElseThrow(() -> new EntityNotFoundException("답변이 존재하지 않습니다."));
        }

        List<Image> images = imageRepository.findAllByBoardId(boardId);

        return ConsumerBoardDetailResponse.from(board, images);

    }

}
