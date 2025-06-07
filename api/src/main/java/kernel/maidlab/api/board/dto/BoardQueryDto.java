package kernel.maidlab.api.board.dto;

import com.querydsl.core.annotations.QueryProjection;
import kernel.maidlab.api.board.eum.BoardType;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BoardQueryDto {

    private Long consumerId;
    private String title;
    private String content;
    private BoardType boardType;
    private boolean answered;

    @QueryProjection
    public BoardQueryDto(Long consumerId, String title, String content, BoardType boardType, boolean answered) {
        this.consumerId = consumerId;
        this.title = title;
        this.content = content;
        this.boardType = boardType;
        this.answered = answered;
    }

}
