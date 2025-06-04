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
public class ConsumerBoardDetailResponseDto {

    private String title;
    private String content;
    private boolean answered;
    private BoardType boardType;
//    private LocalDateTime createAt
    private List<ImageDto> images;
    private AnswerResponseDto answer;


    public static ConsumerBoardDetailResponseDto from (Board board, List<Image> images){

        return new ConsumerBoardDetailResponseDto(
                board.getTitle(),
                board.getContent(),
                board.isAnswered(),
                board.getBoardType(),
                images.stream()
                        .map(ImageDto::from)
                        .toList(),
                board.isAnswered() ? AnswerResponseDto.from(board.getAnswer()) : null
        );
    }

}
