package kernel.maidlab.domain.board.dto;

import java.time.LocalDateTime;

import com.querydsl.core.annotations.QueryProjection;

import kernel.maidlab.domain.board.enums.BoardType;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BoardQueryDto {

	private Long boardId;
	private String title;
	private String content;
	private BoardType boardType;
	private boolean answered;
	private LocalDateTime createdAt;

	private String answerContent;
	private LocalDateTime answerCreatedAt;

	@QueryProjection
	public BoardQueryDto(
		Long boardId,
		String title,
		String content,
		BoardType boardType,
		boolean answered,
		LocalDateTime createdAt,
		String answerContent,
		LocalDateTime answerCreatedAt
	) {
		this.boardId = boardId;
		this.title = title;
		this.content = content;
		this.boardType = boardType;
		this.answered = answered;
		this.createdAt = createdAt;
		this.answerContent = answerContent;
		this.answerCreatedAt = answerCreatedAt;
	}

}
