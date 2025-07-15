package kernel.maidlab.admin.board.repository.support;

import kernel.maidlab.domain.board.entity.BoardImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AdminImageRepository extends JpaRepository<BoardImage, Long> {

	List<BoardImage> findAllByBoardId(Long boardId);
}
