package kernel.maidlab.admin.board.service;

import java.nio.file.AccessDeniedException;
import java.util.List;

import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import kernel.maidlab.api.board.dto.request.AnswerRequestDto;
import kernel.maidlab.api.board.dto.request.BoardRequestDto;
import kernel.maidlab.api.board.dto.request.BoardUpdateRequestDto;
import kernel.maidlab.api.board.dto.response.BoardDetailResponseDto;
import kernel.maidlab.api.board.dto.response.BoardResponseDto;
import kernel.maidlab.api.board.entity.Board;
import kernel.maidlab.api.board.entity.Image;
import kernel.maidlab.api.board.repository.BoardRepository;
import kernel.maidlab.api.board.repository.ImageRepository;
import kernel.maidlab.api.board.service.BoardService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminBoardService {

	private final BoardRepository boardRepository;
	private final ImageRepository imageRepository;

	public BoardDetailResponseDto adminGetConsumerBoard(
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

		return BoardDetailResponseDto.from(board, images);

	}
}
