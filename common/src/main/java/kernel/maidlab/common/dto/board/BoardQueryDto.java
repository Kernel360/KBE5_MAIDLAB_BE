package kernel.maidlab.common.dto.board;

import com.querydsl.core.annotations.QueryProjection;
import kernel.maidlab.common.enums.BoardType;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class BoardQueryDto {

    private Long boardId;
    private String title;
    private String content;
    private BoardType boardType;
    private boolean answered;
    private LocalDateTime createdAt;

    @QueryProjection
    public BoardQueryDto(Long boardId, String title, String content, BoardType boardType, boolean answered, LocalDateTime createdAt) {
        this.boardId = boardId;
        this.title = title;
        this.content = content;
        this.boardType = boardType;
        this.answered = answered;
        this.createdAt = createdAt;
    }

}
