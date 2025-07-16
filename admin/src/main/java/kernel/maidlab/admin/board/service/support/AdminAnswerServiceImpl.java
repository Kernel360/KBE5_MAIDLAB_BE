package kernel.maidlab.admin.board.service.support;

import java.util.Optional;

import org.springframework.stereotype.Service;

import kernel.maidlab.core.aop.annotation.exception.ExceptionHandler;
import kernel.maidlab.core.aop.annotation.exception.Retry;
import kernel.maidlab.core.aop.enums.LogLevel;
import kernel.maidlab.common.enums.ResponseType;
import kernel.maidlab.admin.board.repository.support.AdminAnswerRepository;
import kernel.maidlab.domain.board.entity.Answer;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminAnswerServiceImpl implements AdminAnswerService {

	private final AdminAnswerRepository adminAnswerRepository;

	@Override
	@Retry(
		maxAttempts = 3,
		delay = 1000,
		retryFor = {org.springframework.dao.DataAccessException.class}
	)
	@ExceptionHandler(
		value = {RuntimeException.class},
		responseType = ResponseType.DATABASE_ERROR,
		message = "답변 저장 중 오류가 발생했습니다",
		logLevel = LogLevel.ERROR
	)
	public Answer save(Answer answer) {
		return adminAnswerRepository.save(answer);
	}

	@Override
	@ExceptionHandler(
		value = {RuntimeException.class},
		responseType = ResponseType.DATABASE_ERROR,
		message = "답변 조회 중 오류가 발생했습니다",
		logLevel = LogLevel.WARN
	)
	public Optional<Answer> findById(Long Id) {
		return adminAnswerRepository.findById(Id);
	}

}
