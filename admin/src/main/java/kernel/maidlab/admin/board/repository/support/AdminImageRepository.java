package kernel.maidlab.admin.board.repository.support;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import kernel.maidlab.common.entity.board.Image;

public interface AdminImageRepository extends JpaRepository<Image, Long> {

	List<Image> findAllByBoardId(Long boardId);
}
