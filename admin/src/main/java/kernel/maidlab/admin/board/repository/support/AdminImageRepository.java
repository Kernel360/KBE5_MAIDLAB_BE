package kernel.maidlab.admin.board.repository.support;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import kernel.maidlab.common.entity.board.BoardImage;

public interface AdminImageRepository extends JpaRepository<BoardImage, Long> {

	List<BoardImage> findAllByBoardId(Long boardId);
}
