package kernel.maidlab.domain.board.repository;

import kernel.maidlab.domain.board.entity.Board;
import kernel.maidlab.domain.board.entity.BoardImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import java.util.List;

public interface ImageRepository extends JpaRepository<BoardImage, Long> {

	List<BoardImage> findAllByBoardId(Long boardId);

	@Modifying
	void deleteAllByBoard(Board board);
}
