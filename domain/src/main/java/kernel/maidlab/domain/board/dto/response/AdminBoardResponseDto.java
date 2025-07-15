package kernel.maidlab.domain.board.dto.response;

import kernel.maidlab.domain.board.dto.AdminBoardQueryDto;
import kernel.maidlab.domain.board.entity.Board;
import kernel.maidlab.common.enums.BoardType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AdminBoardResponseDto {

	private Long boardId;
	private String title;
	private String content;
	private boolean isAnswered;
	private BoardType boardType;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
	private String managerName;
	private String consumerName;

	// 정적 팩토리 메서드
	public static AdminBoardResponseDto from(AdminBoardQueryDto boardQueryDto) {

		AdminBoardResponseDto adminBoardDto = new AdminBoardResponseDto();
		adminBoardDto.boardId = boardQueryDto.getBoardId();
		adminBoardDto.title = boardQueryDto.getTitle();
		adminBoardDto.content = boardQueryDto.getContent();
		adminBoardDto.isAnswered = boardQueryDto.isAnswered();
		adminBoardDto.boardType = boardQueryDto.getBoardType();
		adminBoardDto.createdAt = boardQueryDto.getCreatedAt();
		adminBoardDto.updatedAt = boardQueryDto.getUpdatedAt();
		adminBoardDto.managerName = boardQueryDto.getManagerName();
		adminBoardDto.consumerName = boardQueryDto.getConsumerName();
		return adminBoardDto;
	}

	public static AdminBoardResponseDto fromBoard(Board board) {

		AdminBoardResponseDto adminBoardDto = new AdminBoardResponseDto();
		adminBoardDto.boardId = board.getId();
		adminBoardDto.title = board.getTitle();
		adminBoardDto.content = board.getContent();
		adminBoardDto.isAnswered = board.getIsAnswered();
		adminBoardDto.boardType = board.getBoardType();
		adminBoardDto.createdAt = board.getCreatedAt();
		adminBoardDto.updatedAt = board.getUpdatedAt();
		adminBoardDto.managerName = board.getManager() != null ? board.getManager().getName() : null;
		adminBoardDto.consumerName = board.getConsumer() != null ? board.getConsumer().getName() : null;
		return adminBoardDto;
	}
}
