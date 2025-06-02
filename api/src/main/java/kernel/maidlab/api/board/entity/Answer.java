package kernel.maidlab.api.board.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import kernel.maidlab.common.entity.Base;
import lombok.Getter;

@Entity
@Getter
public class Answer extends Base {

    @Column(nullable = false, columnDefinition = "TEXT")
    private String answer;

    @OneToOne
    @JoinColumn(name = "board_id")
    private Board board;
}
