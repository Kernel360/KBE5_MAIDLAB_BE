package kernel.maidlab.api.board.repository;

import kernel.maidlab.api.board.dto.BoardQueryDto;
import kernel.maidlab.api.board.entity.Board;
import kernel.maidlab.common.enums.UserType;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BoardRepositoryCustom {

	List<BoardQueryDto> findAllByUserIdIsDeletedFalse(Long userId, UserType userType);

	Optional<Board> findBoardWithAnswerIfAnswered(@Param("boardId") Long boardId);
}
