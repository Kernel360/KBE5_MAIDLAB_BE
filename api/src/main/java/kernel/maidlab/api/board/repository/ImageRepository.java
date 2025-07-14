package kernel.maidlab.api.board.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import kernel.maidlab.common.entity.board.Board;
import kernel.maidlab.common.entity.board.BoardImage;

public interface ImageRepository extends JpaRepository<BoardImage, Long> {

	List<BoardImage> findAllByBoardId(Long boardId);

	@Modifying
	void deleteAllByBoard(Board board);
}
