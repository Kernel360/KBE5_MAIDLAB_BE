package kernel.maidlab.api.board.dto.response;

import kernel.maidlab.api.board.dto.BoardQueryDto;
import kernel.maidlab.api.board.eum.BoardType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BoardResponseDto {

    private String title;
    private String content;
    private boolean isAnswered;
    private BoardType boardType;
    //createAt - Base엔티티 수정시 추가 예정

    // 정적 팩토리 메서드
    public static BoardResponseDto from(BoardQueryDto boardQueryDto){

        BoardResponseDto boardDto = new BoardResponseDto();
        boardDto.title = boardQueryDto.getTitle();
        boardDto.content = boardQueryDto.getContent();
        boardDto.isAnswered = boardQueryDto.isAnswered();
        boardDto.boardType = boardQueryDto.getBoardType();
        return boardDto;
    }
}
