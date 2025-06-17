package kernel.maidlab.api.board.repository;

import kernel.maidlab.common.dto.board.BoardQueryDto;
import kernel.maidlab.common.enums.UserType;

import java.util.List;

public interface BoardRepositoryCustom {

    List<BoardQueryDto> findAllByUserIdIsDeletedFalse(Long userId, UserType userType);
}
