package kernel.maidlab.domain.board.entity;

import java.util.Collections;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import kernel.maidlab.common.entity.TimeBase;
import kernel.maidlab.domain.board.enums.BoardType;
import kernel.maidlab.domain.board.dto.request.BoardRequestDto;
import kernel.maidlab.domain.board.dto.request.BoardUpdateRequestDto;
import kernel.maidlab.domain.consumer.entity.Consumer;
import kernel.maidlab.domain.manager.entity.Manager;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

	public boolean getIsAnswered() {
		return isAnswered;
	}

	public boolean getIsDeleted() {
		return isDeleted;
	}

	public void updateIsDelete(boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	public Board(Consumer consumer, BoardType boardType, String title, String content) {
		this.consumer = consumer;
		this.boardType = boardType;
		this.title = title;
		this.content = content;
	}

	public Board(Manager manager, BoardType boardType, String title, String content) {
		this.manager = manager;
		this.boardType = boardType;
		this.title = title;
		this.content = content;
	}

	public static Board createBoard(Consumer consumer, BoardRequestDto boardRequestDto) {
		return new Board(
			consumer,
			boardRequestDto.getBoardType(),
			boardRequestDto.getTitle(),
			boardRequestDto.getContent()
		);
	}

	public static Board createBoard(Manager manager, BoardRequestDto boardRequestDto) {
		return new Board(
			manager,
			boardRequestDto.getBoardType(),
			boardRequestDto.getTitle(),
			boardRequestDto.getContent()
		);
	}

	public static Board createBoard(Object user, BoardRequestDto boardRequestDto) {
		return switch (user) {
			case Consumer consumer -> createBoard(consumer, boardRequestDto);
			case Manager manager -> createBoard(manager, boardRequestDto);
			default -> throw new IllegalArgumentException("지원하지 않는 사용자 타입입니다.");
		};
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

	public boolean isAccessibleBy(Consumer consumer) {
		return this.consumer != null && this.consumer.getId().equals(consumer.getId());
	}

	public boolean isAccessibleBy(Manager manager) {
		return this.manager != null && this.manager.getId().equals(manager.getId());
	}

	public boolean isAccessibleBy(Object user) {
		return switch (user) {
			case Consumer consumer -> isAccessibleBy(consumer);
			case Manager manager -> isAccessibleBy(manager);
			default -> false;
		};
	}
}
