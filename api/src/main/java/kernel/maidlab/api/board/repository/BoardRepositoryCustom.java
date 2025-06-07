package kernel.maidlab.api.board.repository;

import feign.Param;
import kernel.maidlab.api.board.dto.BoardQueryDto;
import kernel.maidlab.common.enums.UserType;

import java.util.List;

public interface BoardRepositoryCustom {

    List<BoardQueryDto> findAllByUserIdIsDeletedFalse(Long userId, UserType userType);
}
