package kernel.maidlab.common.dto.board.response;

import java.time.LocalDateTime;
import java.util.List;

import kernel.maidlab.common.dto.board.ImageDto;
import kernel.maidlab.common.entity.board.Board;
import kernel.maidlab.common.entity.board.BoardImage;
import kernel.maidlab.common.enums.BoardType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AdminBoardDetailResponseDto {

	private String title;
	private String content;
	private boolean isAnswered;
	private BoardType boardType;
	private LocalDateTime createdAt;
	private List<ImageDto> images;
	private AnswerResponseDto answer;
	private String managerName;
	private String consumerName;

	public static AdminBoardDetailResponseDto from(Board board, List<BoardImage> boardImages) {

		return new AdminBoardDetailResponseDto(
			board.getTitle(),
			board.getContent(),
			board.getIsAnswered(),
			board.getBoardType(),
			board.getCreatedAt(),
			boardImages.stream()
				.map(ImageDto::from)
				.toList(),
			board.getIsAnswered() ? AnswerResponseDto.from(board.getAnswer()) : null,
			board.getManager() != null ? board.getManager().getName() : null,
			board.getConsumer() != null ? board.getConsumer().getName() : null
		);
	}

}
