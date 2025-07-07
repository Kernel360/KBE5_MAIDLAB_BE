package kernel.maidlab.admin.board.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import kernel.maidlab.admin.board.repository.support.querydsl.AdminBoardRepositoryCustom;
import kernel.maidlab.common.entity.board.Board;

public interface AdminBoardRepository extends JpaRepository<Board, Long>, AdminBoardRepositoryCustom {

	List<Board> findAllByManagerIdNullAndIsDeletedFalse(Pageable pageable);

	Board findByIdAndIsDeletedFalse(Long boardId);

	List<Board> findAllByConsumerIdNull(Pageable pageable);

	Long countByIsAnsweredFalseAndIsDeletedFalse();

	Long countByManagerIdNullAndIsAnsweredFalseAndIsDeletedFalse();

	Long countByConsumerIdNullAndIsAnsweredFalseAndIsDeletedFalse();
}
