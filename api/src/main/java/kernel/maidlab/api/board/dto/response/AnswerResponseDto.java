package kernel.maidlab.api.board.dto.response;

import kernel.maidlab.api.board.entity.Answer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AnswerResponseDto {
    private String content;
//    private LocalDateTime createAt;

    public static AnswerResponseDto from(Answer answer){
        return new AnswerResponseDto(
                answer.getContent()
        );

    }
}
