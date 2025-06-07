package kernel.maidlab.api.board.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kernel.maidlab.api.board.dto.BoardQueryDto;
import kernel.maidlab.api.board.dto.QBoardQueryDto;
import kernel.maidlab.api.board.entity.QBoard;
import kernel.maidlab.common.enums.UserType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class BoardRepositoryCustomImpl implements BoardRepositoryCustom{

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<BoardQueryDto> findAllByUserIdIsDeletedFalse(Long userId, UserType userType) {
        QBoard board = QBoard.board;

        BooleanExpression condition = (userType == UserType.CONSUMER)
                ? board.consumer.id.eq(userId)
                : board.manager.id.eq(userId);

        BooleanExpression notDeleted = board.isDeleted.isFalse();

        return jpaQueryFactory
                .select(new QBoardQueryDto(
                        board.id,
                        board.title,
                        board.content,
                        board.boardType,
                        board.isAnswered
                ))
                .from(board)
                .where(condition.and(notDeleted))
                .fetch();
    }
}
