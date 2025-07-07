package kernel.maidlab.common.entity.board;

import jakarta.persistence.*;
import kernel.maidlab.common.entity.base.TimeBase;
import kernel.maidlab.common.entity.consumer.Consumer;
import kernel.maidlab.common.entity.manager.Manager;
import kernel.maidlab.common.entity.base.UserBase;
import kernel.maidlab.common.dto.board.request.BoardRequestDto;
import kernel.maidlab.common.dto.board.request.BoardUpdateRequestDto;
import kernel.maidlab.common.enums.BoardType;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Collections;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor
public class Board extends TimeBase {

    @ManyToOne
    @JoinColumn(name = "consumer_id")
    private Consumer consumer;

    @ManyToOne
    @JoinColumn(name = "manager_id")
    private Manager manager;

   @OneToOne(mappedBy = "board", cascade = CascadeType.ALL)
    private Answer answer;

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BoardImage> boardImages;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BoardType boardType;

    @Column(nullable = false, length = 500)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(name = "is_answered", nullable = false)
    private boolean isAnswered;

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

    public Board(UserBase user, BoardType boardType, String title, String content) {
        if (user instanceof Consumer consumer) {
            this.consumer = consumer;
        } else if (user instanceof Manager manager) {
            this.manager = manager;
        } else {
            throw new IllegalArgumentException("지원하지 않는 사용자 타입입니다.");
        }

        this.boardType = boardType;
        this.title = title;
        this.content = content;
    }

    public static Board createBoard(UserBase user, BoardRequestDto boardRequestDto){

        if (user instanceof Consumer) {
            return new Board(
                    (Consumer) user,
                    boardRequestDto.getBoardType(),
                    boardRequestDto.getTitle(),
                    boardRequestDto.getContent()
            );

        } else if (user instanceof Manager) {
            return new Board(
                    (Manager) user,
                    boardRequestDto.getBoardType(),
                    boardRequestDto.getTitle(),
                    boardRequestDto.getContent());

        } else {
            throw new IllegalArgumentException("지원하지 않는 사용자 타입입니다.");
        }
    }

    public List<BoardImage> getBoardImages() {
        return boardImages != null ? boardImages : Collections.emptyList();
    }

    public void boardUpdate(BoardUpdateRequestDto dto) {
        this.title = dto.getTitle();
        this.content = dto.getContent();
        this.boardType = dto.getBoardType();
    }

    public void makeAnswer() {
        this.isAnswered = true;
    }

    public boolean isAccessibleBy(UserBase user) {
        if (user instanceof Consumer consumer) {
            return this.consumer != null && this.consumer.getId().equals(consumer.getId());
        }
        if (user instanceof Manager manager) {
            return this.manager != null && this.manager.getId().equals(manager.getId());
        }
        return false;
    }
}
