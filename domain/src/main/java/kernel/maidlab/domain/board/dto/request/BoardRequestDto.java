package kernel.maidlab.domain.board.dto.request;

import java.util.List;

import kernel.maidlab.common.enums.BoardType;
import kernel.maidlab.domain.board.dto.ImageDto;
import lombok.Getter;

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
