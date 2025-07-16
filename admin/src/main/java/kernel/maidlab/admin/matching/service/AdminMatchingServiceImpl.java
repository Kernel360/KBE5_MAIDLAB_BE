package kernel.maidlab.admin.matching.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import kernel.maidlab.core.aop.annotation.exception.ExceptionHandler;
import kernel.maidlab.core.aop.annotation.exception.Retry;
import kernel.maidlab.core.aop.enums.LogLevel;
import kernel.maidlab.common.enums.ResponseType;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import kernel.maidlab.admin.matching.repository.AdminMatchingRepository;
import kernel.maidlab.common.enums.Status;
import kernel.maidlab.domain.matching.dto.response.MatchingResponseDto;
import kernel.maidlab.domain.matching.entity.Matching;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminMatchingServiceImpl implements AdminMatchingService {

	private final AdminMatchingRepository adminMatchingRepository;

	@Override
	@Retry(
		maxAttempts = 2,
		delay = 500,
		retryFor = {org.springframework.dao.DataAccessException.class}
	)
	@ExceptionHandler(
		value = {RuntimeException.class},
		responseType = ResponseType.DATABASE_ERROR,
		message = "매칭 데이터 조회 중 오류가 발생했습니다",
		logLevel = LogLevel.ERROR
	)
	public List<MatchingResponseDto> allMatching(HttpServletRequest request, int page, int size) {
		Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "updatedAt"));
		Page<Matching> matchings = adminMatchingRepository.findAll(pageable);
		return matchings.stream()
			.map(matching -> new MatchingResponseDto(
				matching.getReservationId(),
				matching.getManagerId(),
				matching.getMatchingStatus(),
				matching.getMatchingCount(),
				matching.getUpdatedAt()
			))
			.toList();
	}

	@Override
	public List<MatchingResponseDto> statusMatching(Status status, int page, int size) {
		Page<Matching> matchings;
		Pageable pageable = PageRequest.of(page, size);
		matchings = adminMatchingRepository.findAllByMatchingStatusOrderByUpdatedAtDesc(status, pageable);
		return matchings.stream()
			.map(matching -> new MatchingResponseDto(
				matching.getReservationId(),
				matching.getManagerId(),
				matching.getMatchingStatus(),
				matching.getMatchingCount(),
				matching.getUpdatedAt()
			))
			.toList();
	}

	@Transactional
	@Override
	@Retry(
		maxAttempts = 3,
		delay = 1000,
		retryFor = {org.springframework.dao.DataAccessException.class}
	)
	@ExceptionHandler(
		value = {RuntimeException.class, NullPointerException.class},
		responseType = ResponseType.DATABASE_ERROR,
		message = "매니저 변경 처리 중 오류가 발생했습니다",
		logLevel = LogLevel.ERROR
	)
	public void changeManager(Long reservationId, Long managerId) {
		Matching matching = adminMatchingRepository.findByReservationId(reservationId);
		if (matching == null) {
			throw new NullPointerException("매칭 데이터를 찾을 수 없습니다. 예약 ID: " + reservationId);
		}
		matching.setManagerId(managerId);
		matching.setMatchingStatus(Status.PENDING);
	}

}
