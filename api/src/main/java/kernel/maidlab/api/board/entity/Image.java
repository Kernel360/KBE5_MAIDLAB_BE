package kernel.maidlab.api.board.entity;

import jakarta.persistence.Entity;
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
    private Board board;
    private String imageUrl;
}
