package kernel.maidlab.api.board.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import kernel.maidlab.api.board.dto.ImageDto;
import kernel.maidlab.common.entity.Base;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Image extends Base {

    @ManyToOne
    @JoinColumn(name = "board_id")
    private Board board;

    @Column(name = "image_path", nullable = false)
    private String imagePath;

    @Column(nullable = false)
    private String name;

    public void changeBoard(Board board) {
        this.board = board;
    }

    public void updateImage(ImageDto imageDto) {
        this.name = imageDto.getName();
        this.imagePath = imageDto.getImagePath();
    }
}
