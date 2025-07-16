package kernel.maidlab.domain.board.dto.response;

import java.time.LocalDateTime;

import kernel.maidlab.domain.board.entity.Answer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
