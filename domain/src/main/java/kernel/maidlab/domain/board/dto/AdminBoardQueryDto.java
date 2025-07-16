package kernel.maidlab.domain.board.dto;

import java.time.LocalDateTime;

import com.querydsl.core.annotations.QueryProjection;

import kernel.maidlab.domain.board.enums.BoardType;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AdminBoardQueryDto {

	private Long boardId;
	private String title;
	private String content;
	private BoardType boardType;
	private boolean answered;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
	private String consumerName;
	private String managerName;

	@QueryProjection
	public AdminBoardQueryDto(Long boardId, String title, String content, BoardType boardType, boolean answered,
		LocalDateTime createdAt, LocalDateTime updatedAt, String consumerName, String managerName) {
		this.boardId = boardId;
		this.title = title;
		this.content = content;
		this.boardType = boardType;
		this.answered = answered;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
		this.consumerName = consumerName;
		this.managerName = managerName;
	}
}
