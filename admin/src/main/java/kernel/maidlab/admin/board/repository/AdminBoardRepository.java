package kernel.maidlab.admin.board.repository;

import kernel.maidlab.admin.board.repository.support.querydsl.AdminBoardRepositoryCustom;
import kernel.maidlab.api.board.entity.Board;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AdminBoardRepository extends JpaRepository<Board, Long>, AdminBoardRepositoryCustom {

	List<Board> findAllByManagerIdNullAndIsDeletedFalse(Pageable pageable);

	Board findByIdAndIsDeletedFalse(Long boardId);

	List<Board> findAllByConsumerIdNull(Pageable pageable);

	Long countByIsAnsweredFalseAndIsDeletedFalse();

	Long countByManagerIdNullAndIsAnsweredFalseAndIsDeletedFalse();

	Long countByConsumerIdNullAndIsAnsweredFalseAndIsDeletedFalse();
}
