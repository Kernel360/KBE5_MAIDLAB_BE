package kernel.maidlab.api.board.dto.response;

import kernel.maidlab.api.board.entity.Answer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AnswerResponseDto {
	private String content;
	private LocalDateTime createdAt;

	public static AnswerResponseDto from(Answer answer) {
		return new AnswerResponseDto(
			answer.getContent(),
			answer.getCreatedAt()
		);

	}
}
