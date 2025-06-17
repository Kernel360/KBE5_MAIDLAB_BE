package kernel.maidlab.admin.board.service;

import java.util.Optional;

import kernel.maidlab.common.entity.board.Answer;

public interface AdminAnswerService {
	Answer save(Answer answer);

	Optional<Answer> findById(Long Id);
}
