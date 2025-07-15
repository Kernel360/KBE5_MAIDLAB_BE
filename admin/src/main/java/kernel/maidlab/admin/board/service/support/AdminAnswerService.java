package kernel.maidlab.admin.board.service.support;

import java.util.Optional;

import kernel.maidlab.domain.board.entity.Answer;

public interface AdminAnswerService {
	Answer save(Answer answer);

	Optional<Answer> findById(Long Id);
}
