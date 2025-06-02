package kernel.maidlab.api.board.dto.request;

import kernel.maidlab.api.board.eum.BoardType;
import lombok.Getter;

import java.util.List;

@Getter
public class ConsumerBoardRequestDto {

    private BoardType boardType;
    private String title;
    private String content;
    private List<String> imageUrls;
}
