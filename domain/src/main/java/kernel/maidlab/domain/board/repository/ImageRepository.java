package kernel.maidlab.domain.board.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import kernel.maidlab.domain.board.entity.Board;
import kernel.maidlab.domain.board.entity.BoardImage;

public interface ImageRepository extends JpaRepository<BoardImage, Long> {

	List<BoardImage> findAllByBoardId(Long boardId);

	@Modifying
	void deleteAllByBoard(Board board);
}
