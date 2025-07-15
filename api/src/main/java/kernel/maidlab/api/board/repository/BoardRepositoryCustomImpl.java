package kernel.maidlab.api.board.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kernel.maidlab.api.board.dto.BoardQueryDto;
import kernel.maidlab.api.board.dto.QBoardQueryDto;
import kernel.maidlab.api.board.entity.Board;
import kernel.maidlab.api.board.entity.QAnswer;
import kernel.maidlab.api.board.entity.QBoard;
import kernel.maidlab.common.enums.UserType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static kernel.maidlab.api.board.entity.QAnswer.answer;
import static kernel.maidlab.api.board.entity.QBoard.board;

@Repository
@RequiredArgsConstructor
public class BoardRepositoryCustomImpl implements BoardRepositoryCustom {

	private final JPAQueryFactory jpaQueryFactory;

	@Override
	public List<BoardQueryDto> findAllByUserIdIsDeletedFalse(Long userId, UserType userType) {
		QBoard board = QBoard.board;
		QAnswer answer = QAnswer.answer;

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
				board.isAnswered,
				board.createdAt,
				answer.content,
				answer.createdAt
			))
			.from(board)
			.leftJoin(answer).on(answer.board.eq(board))
			.where(condition.and(notDeleted))
			.fetch();
	}

	@Override
	public Optional<Board> findBoardWithAnswerIfAnswered(Long boardId) {
		return Optional.ofNullable(jpaQueryFactory
			.selectFrom(board)
			.leftJoin(board.answer, answer).fetchJoin()
			.where(
				board.id.eq(boardId),
				board.isDeleted.isFalse(),
				board.isAnswered.isTrue()
			)
			.fetchOne()
		);
	}
}
