package kernel.maidlab.api.board.repository;

import kernel.maidlab.api.board.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ImageRepository extends JpaRepository<Image, Long> {

    List<Image> findAllByBoardId(Long boardId);
}
