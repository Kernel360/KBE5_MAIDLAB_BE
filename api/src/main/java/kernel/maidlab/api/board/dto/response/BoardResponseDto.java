package kernel.maidlab.api.board.dto.response;

import kernel.maidlab.api.board.dto.BoardQueryDto;
import kernel.maidlab.api.board.entity.Board;
import kernel.maidlab.api.board.eum.BoardType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BoardResponseDto {

	private Long boardId;
	private String title;
	private String content;
	private boolean isAnswered;
	private BoardType boardType;
	//createAt - Base엔티티 수정시 추가 예정

	// 정적 팩토리 메서드
	public static BoardResponseDto from(BoardQueryDto boardQueryDto) {

		BoardResponseDto boardDto = new BoardResponseDto();
		boardDto.boardId = boardQueryDto.getBoardId();
		boardDto.title = boardQueryDto.getTitle();
		boardDto.content = boardQueryDto.getContent();
		boardDto.isAnswered = boardQueryDto.isAnswered();
		boardDto.boardType = boardQueryDto.getBoardType();
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
