package kernel.maidlab.api.board.dto;

import kernel.maidlab.api.board.entity.Image;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ImageDto {

    private String imagePath;
    private String name;

    public static ImageDto from(Image image){
        return new ImageDto(
                image.getImagePath(),
                image.getName()
        );
    }
}
