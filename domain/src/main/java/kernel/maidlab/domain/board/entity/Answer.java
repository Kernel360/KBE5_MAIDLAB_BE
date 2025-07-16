package kernel.maidlab.domain.board.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import kernel.maidlab.common.entity.TimeBase;
import kernel.maidlab.domain.board.dto.request.AnswerRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Answer extends TimeBase {

	@OneToOne
	@JoinColumn(name = "board_id")
	private Board board;

	@Column(nullable = false, columnDefinition = "TEXT")
	private String content;

	public Answer(Board board, String content) {
		this.board = board;
		this.content = content;
	}

	public static Answer createAnswer(AnswerRequestDto requestDto, Board board) {
		return new Answer(
			board,
			requestDto.getContent()
		);
	}

	public void setContent(AnswerRequestDto requestDto) {
		this.content = requestDto.getContent();
	}
}
