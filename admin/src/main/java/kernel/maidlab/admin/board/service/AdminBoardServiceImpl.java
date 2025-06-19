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
import kernel.maidlab.admin.board.repository.AdminBoardRepository;
import kernel.maidlab.admin.board.service.support.AdminAnswerService;
import kernel.maidlab.admin.board.service.support.AdminImageServiceImpl;
import kernel.maidlab.common.dto.ResponseDto;
import kernel.maidlab.common.dto.board.request.AnswerRequestDto;
import kernel.maidlab.common.dto.board.response.BoardDetailResponseDto;
import kernel.maidlab.common.dto.board.response.BoardResponseDto;
import kernel.maidlab.common.entity.board.Answer;
import kernel.maidlab.common.entity.board.Board;
import kernel.maidlab.common.entity.board.Image;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminBoardServiceImpl implements AdminBoardService {

	private final AdminBoardRepository adminBoardRepository;
	private final AdminImageServiceImpl adminImageService;
	private final AdminAnswerService adminAnswerService;

	@Override
	public ResponseEntity<ResponseDto<List<BoardResponseDto>>> getAllRefundBoardList(HttpServletRequest request,
		int page, int size) {
		Pageable pageable = PageRequest.of(page, size);
		List<Board> board = adminBoardRepository.findAllByManagerIdNull(pageable);

		return ResponseDto.success(board.stream()
			.map(BoardResponseDto::fromBoard)
			.toList());
	}

	@Override
	public ResponseEntity<ResponseDto<BoardDetailResponseDto>> adminGetConsumerBoard(
		HttpServletRequest request,
		Long boardId
	) throws AccessDeniedException {

		Board board = adminBoardRepository.findByIdAndIsDeletedFalse(boardId);

		// 답변여부가 true면 답변까지 조회
		if (board.getIsAnswered()) {
			board = adminBoardRepository.findBoardWithAnswerIfAnswered(boardId);
			System.out.println(board.getAnswer().getId());
		}

		List<Image> images = adminImageService.findAllByBoardId(boardId);

		return ResponseDto.success(BoardDetailResponseDto.from(board, images));
	}

	@Override
	public ResponseEntity<ResponseDto<List<BoardResponseDto>>> getAllConsultationBoardList(HttpServletRequest request,
		int page, int size) {
		Pageable pageable = PageRequest.of(page, size);
		List<Board> boards = adminBoardRepository.findAllByConsumerIdNull(pageable);

		return ResponseDto.success(boards.stream()
			.map(BoardResponseDto::fromBoard)
			.toList());
	}

	@Override
	public ResponseEntity<ResponseDto<Void>> createAnswer(AnswerRequestDto requestDto, HttpServletRequest request,
		Long boardId) {
		Board board = adminBoardRepository.findById(boardId)
			.orElseThrow(() -> new EntityNotFoundException("게시글을 찾을 수 없습니다. ID: " + boardId));
		board.makeAnswer();
		Answer answer = Answer.createAnswer(requestDto, board);
		adminAnswerService.save(answer);
		return ResponseDto.success();
	}

	@Transactional
	@Override
	public ResponseEntity<ResponseDto<Void>> modifyAnswer(AnswerRequestDto requestDto, Long boardId) {
		Board board = adminBoardRepository.findById(boardId)
			.orElseThrow(() -> new EntityNotFoundException("게시글을 찾을 수 없습니다. boardId: " + boardId));
		Answer answer = adminAnswerService.findById(board.getAnswer().getId())
			.orElseThrow(() -> new EntityNotFoundException("답변을 찾을 수 없습니다. boardId: " + boardId));
		answer.setContent(requestDto);
		return ResponseDto.success();
	}
}
