package kernel.maidlab.api.board.repository;

import feign.Param;
import kernel.maidlab.api.board.dto.BoardQueryDto;

import java.util.List;

public interface BoardRepositoryCustom {

    List<BoardQueryDto> findAllByConsumerUuid(@Param("uuid") String uuid);
}
