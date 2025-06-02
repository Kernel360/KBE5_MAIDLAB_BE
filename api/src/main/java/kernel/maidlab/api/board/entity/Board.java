package kernel.maidlab.api.board.entity;

import jakarta.persistence.*;
import kernel.maidlab.api.auth.entity.Consumer;
import kernel.maidlab.api.auth.entity.Manager;
import kernel.maidlab.api.board.dto.request.ConsumerBoardRequestDto;
import kernel.maidlab.common.entity.Base;
import kernel.maidlab.common.enums.BoardType;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
public class Board extends Base {

    @ManyToOne
    private Consumer consumer;

    @ManyToOne
    private Manager manager;

    @OneToOne(mappedBy = "board",cascade = CascadeType.ALL)
    private Answer answer;

    @Enumerated(EnumType.STRING)
    private BoardType boardType;

    private String title;

    private String content;

    private boolean isAnswer;

    public Board(Consumer consumer, BoardType boardType, String title, String content) {
        this.consumer = consumer;
        this.boardType = boardType;
        this.title = title;
        this.content = content;
    }

    public static Board createConsumerBoard(Consumer consumer, ConsumerBoardRequestDto consumerBoardRequestDto){
        return new Board(
                consumer,
                consumerBoardRequestDto.getBoardType(),
                consumerBoardRequestDto.getTitle(),
                consumerBoardRequestDto.getContent()
        );
    }

}
