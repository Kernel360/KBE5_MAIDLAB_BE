package kernel.maidlab.api.board.repository;

import kernel.maidlab.api.board.dto.BoardQueryDto;
import kernel.maidlab.api.board.entity.Board;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BoardRepository extends JpaRepository<Board, Long>, BoardRepositoryCustom {

    // 나중에 쿼리dsl로 바꿀 예정
    // todo: 나중에 쿼리dsl로 바꿀 예정
    @Query("SELECT b " +
            "FROM Board b " +
            "LEFT JOIN FETCH b.answer " +
            "WHERE b.id = :boardId " +
            "AND b.isAnswered = true " +
            "AND b .isDeleted = false")
    Optional<Board> findBoardWithAnswerIfAnswered(@Param("boardId") Long boardId);

    /**
     * isDeleted = false만 조회하도록
     */
    // 전체 게시판 조회
    List<Board> findAllByIsDeletedFalse();

    // 단건 조회
    Optional<Board> findByIdAndIsDeletedFalse(Long id);

	List<Board> findAllByManagerIdNull(Pageable pageable);

	List<Board> findAllByConsumerIdNull(Pageable pageable);
}
