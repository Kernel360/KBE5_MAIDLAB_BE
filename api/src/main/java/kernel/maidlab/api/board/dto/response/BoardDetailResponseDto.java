package kernel.maidlab.api.board.dto.response;

import kernel.maidlab.api.board.dto.ImageDto;
import kernel.maidlab.api.board.entity.Board;
import kernel.maidlab.api.board.entity.Image;
import kernel.maidlab.api.board.eum.BoardType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BoardDetailResponseDto {

    private String title;
    private String content;
    private boolean isAnswered;
    private BoardType boardType;
//    private LocalDateTime createAt
    private List<ImageDto> images;
    private AnswerResponseDto answer;


    public static BoardDetailResponseDto from (Board board, List<Image> images){

        return new BoardDetailResponseDto(
                board.getTitle(),
                board.getContent(),
                board.getIsAnswered(),
                board.getBoardType(),
                images.stream()
                        .map(ImageDto::from)
                        .toList(),
                board.getIsAnswered() ? AnswerResponseDto.from(board.getAnswer()) : null
        );
    }

}
