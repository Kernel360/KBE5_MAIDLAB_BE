package kernel.maidlab.api.board.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import kernel.maidlab.api.board.dto.BoardQueryDto;
import kernel.maidlab.api.board.dto.QBoardQueryDto;
import kernel.maidlab.api.board.entity.QBoard;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class BoardRepositoryCustomImpl implements BoardRepositoryCustom{

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<BoardQueryDto> findAllByConsumerUuid(String uuid) {
        QBoard board = QBoard.board;

        return jpaQueryFactory
                .select(new QBoardQueryDto(
                        board.consumer.id,
                        board.title,
                        board.content,
                        board.boardType,
                        board.answered
                ))
                .from(board)
                .where(board.consumer.uuid.eq(uuid))
                .fetch();
    }
}
