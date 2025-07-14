package kernel.maidlab.admin.board.repository.support.querydsl;

import static kernel.maidlab.common.entity.board.QAnswer.*;
import static kernel.maidlab.common.entity.board.QBoard.*;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;

import jakarta.persistence.EntityNotFoundException;
import kernel.maidlab.common.entity.board.Board;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class AdminBoardRepositoryCustomImpl implements AdminBoardRepositoryCustom {

	private final JPAQueryFactory queryFactory;

	@Override
	public Board findBoardWithAnswerIfAnswered(Long boardId) {
		return Optional.ofNullable(queryFactory
			.selectFrom(board)
			.leftJoin(board.answer, answer).fetchJoin()
			.where(
				board.id.eq(boardId),
				board.isDeleted.isFalse(),
				board.isAnswered.isTrue()
			)
			.fetchOne()
		).orElseThrow(() -> new EntityNotFoundException("존재하지 않는 게시물 입니다."));
	}
}
