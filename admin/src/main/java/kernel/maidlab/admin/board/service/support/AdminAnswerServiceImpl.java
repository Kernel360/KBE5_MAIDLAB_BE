package kernel.maidlab.admin.board.service.support;

import kernel.maidlab.admin.board.repository.support.AdminAnswerRepository;
import kernel.maidlab.api.board.entity.Answer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AdminAnswerServiceImpl implements AdminAnswerService {

	private final AdminAnswerRepository adminAnswerRepository;

	@Override
	public Answer save(Answer answer) {
		return adminAnswerRepository.save(answer);
	}

	@Override
	public Optional<Answer> findById(Long Id) {
		return adminAnswerRepository.findById(Id);
	}

}
