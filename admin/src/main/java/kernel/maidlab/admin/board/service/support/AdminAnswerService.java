package kernel.maidlab.admin.board.service.support;

import kernel.maidlab.api.board.entity.Answer;

import java.util.Optional;



public interface AdminAnswerService {
	Answer save(Answer answer);

	Optional<Answer> findById(Long Id);
}
