package kernel.maidlab.common.dto.board.response;

import kernel.maidlab.common.entity.board.Answer;
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
