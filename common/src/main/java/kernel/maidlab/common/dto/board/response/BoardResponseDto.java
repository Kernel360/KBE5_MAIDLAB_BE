package kernel.maidlab.common.dto.board.response;

import kernel.maidlab.common.dto.board.BoardQueryDto;
import kernel.maidlab.common.entity.board.Board;
import kernel.maidlab.common.enums.BoardType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BoardResponseDto {

	private Long boardId;
	private String title;
	private String content;
	private boolean isAnswered;
	private BoardType boardType;
	private LocalDateTime createdAt;

	// 정적 팩토리 메서드
	public static BoardResponseDto from(BoardQueryDto boardQueryDto) {

		BoardResponseDto boardDto = new BoardResponseDto();
		boardDto.boardId = boardQueryDto.getBoardId();
		boardDto.title = boardQueryDto.getTitle();
		boardDto.content = boardQueryDto.getContent();
		boardDto.isAnswered = boardQueryDto.isAnswered();
		boardDto.boardType = boardQueryDto.getBoardType();
		boardDto.createdAt = boardQueryDto.getCreatedAt();
		return boardDto;
	}

	public static BoardResponseDto fromBoard(Board board) {

		BoardResponseDto boardDto = new BoardResponseDto();
		boardDto.boardId = board.getId();
		boardDto.title = board.getTitle();
		boardDto.content = board.getContent();
		boardDto.isAnswered = board.getIsAnswered();
		boardDto.boardType = board.getBoardType();
		return boardDto;
	}
}
