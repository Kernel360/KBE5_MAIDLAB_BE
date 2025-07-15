package kernel.maidlab.api.board.repository;

import kernel.maidlab.api.board.entity.Board;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface BoardRepository extends JpaRepository<Board, Long>, BoardRepositoryCustom {

	/**
	 * isDeleted = false만 조회하도록
	 */
	// 전체 게시판 조회
	List<Board> findAllByIsDeletedFalse();

	// 단건 조회
	Optional<Board> findByIdAndIsDeletedFalse(Long id);

	List<Board> findAllByConsumerIdNull(Pageable pageable);
}
