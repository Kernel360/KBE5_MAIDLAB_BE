package kernel.maidlab.admin.board.service;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import kernel.maidlab.common.dto.ResponseDto;
import kernel.maidlab.common.dto.board.request.AnswerRequestDto;
import kernel.maidlab.common.dto.board.response.BoardDetailResponseDto;
import kernel.maidlab.common.dto.board.response.BoardResponseDto;
import kernel.maidlab.common.entity.board.Answer;
import kernel.maidlab.common.entity.board.Board;
import kernel.maidlab.common.entity.board.Image;
import kernel.maidlab.api.board.repository.BoardRepository;
import kernel.maidlab.api.board.repository.ImageRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminBoardServiceImpl implements AdminBoardService {

	private final BoardRepository boardRepository;
	private final ImageRepository imageRepository;
	private final AdminAnswerService adminAnswerService;

	@Override
	public ResponseEntity<ResponseDto<List<BoardResponseDto>>> getAllRefundBoardList(HttpServletRequest request,
		int page, int size) {
		Pageable pageable = PageRequest.of(page, size);
		List<Board> board = boardRepository.findAllByManagerIdNull(pageable);

		return ResponseDto.success(board.stream()
			.map(BoardResponseDto::fromBoard)
			.toList());
	}

	@Override
	public ResponseEntity<ResponseDto<BoardDetailResponseDto>> adminGetConsumerBoard(
		HttpServletRequest request,
		Long boardId
	) throws AccessDeniedException {

		System.out.println(boardId);
		Board board = boardRepository.findByIdAndIsDeletedFalse(boardId)
			.orElseThrow(() -> new EntityNotFoundException("존재하지 않는 게시물 입니다."));

		// 답변여부가 true면 답변까지 조회
		if (board.getIsAnswered()) {
			board = boardRepository.findBoardWithAnswerIfAnswered(boardId)
				.orElseThrow(() -> new EntityNotFoundException("답변이 존재하지 않습니다."));
		}

		List<Image> images = imageRepository.findAllByBoardId(boardId);

		return ResponseDto.success(BoardDetailResponseDto.from(board, images));
	}

	@Override
	public ResponseEntity<ResponseDto<List<BoardResponseDto>>> getAllConsultationBoardList(HttpServletRequest request,
		int page, int size) {
		Pageable pageable = PageRequest.of(page, size);
		List<Board> boards = boardRepository.findAllByConsumerIdNull(pageable);

		return ResponseDto.success(boards.stream()
			.map(BoardResponseDto::fromBoard)
			.toList());
	}

	@Override
	public ResponseEntity<ResponseDto<Void>> createAnswer(AnswerRequestDto requestDto, HttpServletRequest request,
		Long boardId) {
		Optional<Board> board = boardRepository.findById(boardId);
		board.get().makeAnswer();
		Answer answer = Answer.createAnswer(requestDto, board.get());
		adminAnswerService.save(answer);
		return ResponseDto.success();
	}

	@Transactional
	@Override
	public ResponseEntity<ResponseDto<Void>> modifyAnswer(AnswerRequestDto requestDto, Long answerId) {
		Optional<Answer> answer = adminAnswerService.findById(answerId);
		answer.get().setContent(requestDto);
		return ResponseDto.success();
	}
}
