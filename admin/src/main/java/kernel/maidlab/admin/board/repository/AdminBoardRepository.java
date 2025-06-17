package kernel.maidlab.admin.board.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import kernel.maidlab.common.entity.board.Board;

public interface AdminBoardRepository extends JpaRepository<Board, Long> {

}
