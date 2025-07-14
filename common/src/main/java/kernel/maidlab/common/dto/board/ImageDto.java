package kernel.maidlab.common.dto.board;

import kernel.maidlab.common.entity.board.BoardImage;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ImageDto {

	private Long id;
	private String imagePath;
	private String name;

	public static ImageDto from(BoardImage boardImage) {
		return new ImageDto(
			boardImage.getId(),
			boardImage.getImagePath(),
			boardImage.getName()
		);
	}

	@Override
	public String toString() {
		return "ImageDto{" +
			"id=" + id +
			", imagePath='" + imagePath + '\'' +
			", name='" + name + '\'' +
			'}';
	}
}
