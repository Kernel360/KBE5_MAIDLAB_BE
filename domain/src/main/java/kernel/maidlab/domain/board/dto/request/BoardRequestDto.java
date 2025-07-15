package kernel.maidlab.domain.board.dto.request;

import kernel.maidlab.domain.board.dto.ImageDto;
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
