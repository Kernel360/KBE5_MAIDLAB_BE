package kernel.maidlab.domain.board.dto.response;

import kernel.maidlab.domain.board.dto.BoardQueryDto;
import kernel.maidlab.domain.board.enums.BoardType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BoardResponseDto {

    private Long boardId;
    private String title;
    private String content;
    private boolean isAnswered;
    private BoardType boardType;
    private LocalDateTime createdAt;

    private String answerContent;
    private LocalDateTime answerCreatedAt;

    // 정적 팩토리 메서드
    public static BoardResponseDto from(BoardQueryDto boardQueryDto) {

        BoardResponseDto boardDto = new BoardResponseDto();
        boardDto.boardId = boardQueryDto.getBoardId();
        boardDto.title = boardQueryDto.getTitle();
        boardDto.content = boardQueryDto.getContent();
        boardDto.isAnswered = boardQueryDto.isAnswered();
        boardDto.boardType = boardQueryDto.getBoardType();
        boardDto.createdAt = boardQueryDto.getCreatedAt();

        boardDto.answerContent = boardQueryDto.getAnswerContent();
        boardDto.answerCreatedAt = boardQueryDto.getAnswerCreatedAt();

        return boardDto;
    }
}
