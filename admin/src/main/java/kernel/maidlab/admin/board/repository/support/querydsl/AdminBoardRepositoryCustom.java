package kernel.maidlab.admin.board.repository.support.querydsl;

import kernel.maidlab.domain.board.entity.Board;

public interface AdminBoardRepositoryCustom {
	Board findBoardWithAnswerIfAnswered(Long boardId);
}
