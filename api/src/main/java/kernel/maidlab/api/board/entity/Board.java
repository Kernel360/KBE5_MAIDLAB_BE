package kernel.maidlab.api.board.entity;

import jakarta.persistence.*;
import kernel.maidlab.api.auth.entity.Consumer;
import kernel.maidlab.api.auth.entity.Manager;
import kernel.maidlab.api.board.dto.request.BoardUpdateRequestDto;
import kernel.maidlab.api.board.dto.request.BoardRequestDto;
import kernel.maidlab.api.board.eum.BoardType;
import kernel.maidlab.common.entity.Base;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Collections;
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

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Image> images;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BoardType boardType;

    @Column(nullable = false, length = 500)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(name = "is_answered", nullable = false)
    private boolean isAnswered = false;

    @Column(name = "is_deleted", nullable = false)
    private boolean isDeleted;

    public boolean getIsAnswered(){
        return  isAnswered;
    }

    public boolean getIsDeleted(){
        return isDeleted;
    }

    public void updateIsDelete(boolean isDeleted){
        this.isDeleted = isDeleted;
    }

    public Board(Consumer consumer, BoardType boardType, String title, String content) {
        this.consumer = consumer;
        this.boardType = boardType;
        this.title = title;
        this.content = content;
    }

    public static Board createConsumerBoard(Consumer consumer, BoardRequestDto boardRequestDto){
        return new Board(
                consumer,
                boardRequestDto.getBoardType(),
                boardRequestDto.getTitle(),
                boardRequestDto.getContent()
        );
    }

    public List<Image> getImages() {
        return images != null ? images : Collections.emptyList();
    }

    public void boardUpdate(BoardUpdateRequestDto dto) {
        this.title = dto.getTitle();
        this.content = dto.getContent();
        this.boardType = dto.getBoardType();
    }

}
