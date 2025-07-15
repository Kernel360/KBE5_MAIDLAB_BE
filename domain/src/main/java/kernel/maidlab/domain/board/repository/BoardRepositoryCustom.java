package kernel.maidlab.domain.board.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.query.Param;

import kernel.maidlab.common.enums.UserType;
import kernel.maidlab.domain.board.dto.BoardQueryDto;
import kernel.maidlab.domain.board.entity.Board;

public interface BoardRepositoryCustom {

	List<BoardQueryDto> findAllByUserIdIsDeletedFalse(Long userId, UserType userType);

	Optional<Board> findBoardWithAnswerIfAnswered(@Param("boardId") Long boardId);
}
