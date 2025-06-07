package kernel.maidlab.api.board.dto;

import kernel.maidlab.api.board.entity.Image;
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

    public static ImageDto from(Image image){
        return new ImageDto(
                image.getId(),
                image.getImagePath(),
                image.getName()
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
