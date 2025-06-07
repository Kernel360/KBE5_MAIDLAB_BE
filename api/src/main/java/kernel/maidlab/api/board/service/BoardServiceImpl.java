package kernel.maidlab.api.board.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import kernel.maidlab.api.auth.entity.Consumer;
import kernel.maidlab.api.board.dto.BoardQueryDto;
import kernel.maidlab.api.board.dto.request.AnswerRequestDto;
import kernel.maidlab.api.board.dto.request.BoardRequestDto;
import kernel.maidlab.api.board.dto.response.BoardDetailResponseDto;
import kernel.maidlab.api.board.dto.response.BoardResponseDto;
import kernel.maidlab.api.board.entity.Answer;
import kernel.maidlab.api.board.entity.Board;
import kernel.maidlab.api.board.entity.Image;
import kernel.maidlab.api.board.repository.AnswerRepository;
import kernel.maidlab.api.board.repository.BoardRepository;
import kernel.maidlab.api.board.repository.ImageRepository;
import kernel.maidlab.api.util.AuthUtil;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService {

	private final BoardRepository boardRepository;
	private final ImageRepository imageRepository;
	private final AuthUtil authUtil;
    private final AnswerRepository answerRepository;
	// 수요자 게시판 글 생성
	public void createConsumerBoard(HttpServletRequest request,
		BoardRequestDto boardRequestDto) {

		Consumer consumer = authUtil.getConsumer(request);
		Board board = Board.createConsumerBoard(consumer, boardRequestDto);
		boardRepository.save(board);

		boardRequestDto.getImages()
			.forEach((imageDto) -> imageRepository.save(
				new Image(
					board,
					imageDto.getImagePath(),
					imageDto.getName())));
	}

	// 수요자 게시글 전체 조회
	@Transactional(readOnly = true)
	public List<BoardResponseDto> getConsumerBoardList(HttpServletRequest request) {

		String uuid = authUtil.getUuid(request);
		List<BoardQueryDto> boardQueryDto = boardRepository.findAllByConsumerUuid(uuid);

		return boardQueryDto.stream()
			.map(BoardResponseDto::from)
			.toList();
	}

	// 수요자 글 상세 조회
	public BoardDetailResponseDto getConsumerBoard(HttpServletRequest request,
												   Long boardId) throws AccessDeniedException {

		// 검증 로직
		Consumer consumer = authUtil.getConsumer(request);
		Board board = boardRepository.findById(boardId)
			.orElseThrow(() -> new EntityNotFoundException("존재하지 않는 게시물 입니다."));

		// 토큰으로 찾은 수요자id와 PathVariable로 넘어온 게시판id로 찾은 consumerId와 비교
		if (!consumer.getId().equals(board.getConsumer().getId())) {
			throw new AccessDeniedException("해당 게시글에 접근할 권한이 없습니다.");
		}

		// 답변여부가 true면 답변까지 조회
		if (board.isAnswered()) {
			board = boardRepository.findBoardWithAnswerIfAnswered(boardId)
				.orElseThrow(() -> new EntityNotFoundException("답변이 존재하지 않습니다."));
		}

		List<Image> images = imageRepository.findAllByBoardId(boardId);

		return BoardDetailResponseDto.from(board, images);

	}

	@Override
	public List<BoardResponseDto> getAllRefundBoardList(HttpServletRequest request, int page, int size) {
		Pageable pageable = PageRequest.of(page, size);
		List<BoardQueryDto> boardQueryDto = boardRepository.findAllByManagerIdNull(pageable);

		return boardQueryDto.stream()
			.map(BoardResponseDto::from)
			.toList();
	}

	@Override
	public List<BoardResponseDto> getAllConsultationBoardList(HttpServletRequest request, int page, int size) {
		Pageable pageable = PageRequest.of(page, size);
		List<BoardQueryDto> boardQueryDto = boardRepository.findAllByConsumerIdNull(pageable);

		return boardQueryDto.stream()
			.map(BoardResponseDto::from)
			.toList();
	}

	@Override
	public void createAnswer(AnswerRequestDto requestDto, HttpServletRequest request, Long boardId) {

		Optional<Board> board = boardRepository.findById(boardId);
        board.get().makeAnswer();
		Answer answer = Answer.createAnswer(requestDto, board.get());
		answerRepository.save(answer);

	}

	@Transactional
	@Override
	public void modifyAnswer(AnswerRequestDto requestDto, Long answerId) {

		Optional<Answer> answer = answerRepository.findById(answerId);
		answer.get().setContent(requestDto);

	}

}
