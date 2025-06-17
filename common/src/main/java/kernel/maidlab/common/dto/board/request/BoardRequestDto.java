package kernel.maidlab.common.dto.board.request;

import kernel.maidlab.common.dto.board.ImageDto;
import kernel.maidlab.common.enums.BoardType;
import lombok.Getter;

import java.util.List;

@Getter
public class BoardRequestDto {

    private BoardType boardType;
    private String title;
    private String content;
    private List<ImageDto> images;

    @Override
    public String toString() {
        return "ConsumerBoardRequestDto{" +
                "boardType=" + boardType +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", images=" + images +
                '}';
    }
}
