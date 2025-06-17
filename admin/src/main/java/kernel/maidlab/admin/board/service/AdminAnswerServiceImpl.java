package kernel.maidlab.admin.board.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import kernel.maidlab.admin.board.repository.AdminAnswerRepository;
import kernel.maidlab.common.entity.board.Answer;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminAnswerServiceImpl implements AdminAnswerService{

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
