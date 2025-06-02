package kernel.maidlab.api.board.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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

    @Column(name = "image_url", nullable = false)
    private String imageUrl;
}
