package kernel.maidlab.domain.board.repository;

import kernel.maidlab.common.enums.UserType;
import kernel.maidlab.domain.board.dto.BoardQueryDto;
import kernel.maidlab.domain.board.entity.Board;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BoardRepositoryCustom {

    List<BoardQueryDto> findAllByUserIdIsDeletedFalse(Long userId, UserType userType);

    Optional<Board> findBoardWithAnswerIfAnswered(@Param("boardId") Long boardId);
}
