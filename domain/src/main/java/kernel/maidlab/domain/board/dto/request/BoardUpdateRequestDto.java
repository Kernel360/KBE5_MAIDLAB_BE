package kernel.maidlab.domain.board.dto.request;

import kernel.maidlab.domain.board.dto.ImageDto;
import kernel.maidlab.common.enums.BoardType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BoardUpdateRequestDto {

	private String title;
	private String content;
	private BoardType boardType;
	private List<ImageDto> images;

	@Override
	public String toString() {
		return "BoardUpdateRequestDto{" +
			"title='" + title + '\'' +
			", content='" + content + '\'' +
			", boardType=" + boardType +
			", images=" + images +
			'}';
	}
}
