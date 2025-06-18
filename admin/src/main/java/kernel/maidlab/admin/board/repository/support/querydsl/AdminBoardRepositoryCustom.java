package kernel.maidlab.admin.board.repository.support.querydsl;

import kernel.maidlab.common.entity.board.Board;

public interface AdminBoardRepositoryCustom {
	Board findBoardWithAnswerIfAnswered(Long boardId);
}
