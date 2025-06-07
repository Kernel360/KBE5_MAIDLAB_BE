package kernel.maidlab.api.board.repository;

import kernel.maidlab.api.board.entity.Board;
import kernel.maidlab.api.board.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import java.util.List;

public interface ImageRepository extends JpaRepository<Image, Long> {

    List<Image> findAllByBoardId(Long boardId);

    @Modifying
    void deleteAllByBoard(Board board);
}
