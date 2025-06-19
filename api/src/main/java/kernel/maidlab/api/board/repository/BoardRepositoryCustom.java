package kernel.maidlab.api.board.repository;

import kernel.maidlab.common.dto.board.BoardQueryDto;
import kernel.maidlab.common.entity.board.Board;
import kernel.maidlab.common.enums.UserType;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.query.Param;

public interface BoardRepositoryCustom {

    List<BoardQueryDto> findAllByUserIdIsDeletedFalse(Long userId, UserType userType);

    Optional<Board> findBoardWithAnswerIfAnswered(@Param("boardId") Long boardId);
}
