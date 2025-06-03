package kernel.maidlab.api.board.entity;

import jakarta.persistence.*;
import kernel.maidlab.api.auth.entity.Consumer;
import kernel.maidlab.api.auth.entity.Manager;
import kernel.maidlab.api.board.dto.request.ConsumerBoardRequestDto;
import kernel.maidlab.common.entity.Base;
import kernel.maidlab.api.board.eum.BoardType;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Entity
@NoArgsConstructor
public class Board extends Base {

    @ManyToOne
    @JoinColumn(name = "consumer_id")
    private Consumer consumer;

    @ManyToOne
    @JoinColumn(name = "manager_id")
    private Manager manager;

   @OneToOne(mappedBy = "board", cascade = CascadeType.ALL)
    private Answer answer;

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Image> imageList;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BoardType boardType;

    @Column(nullable = false, length = 500)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(nullable = false)
    private boolean answered;

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
