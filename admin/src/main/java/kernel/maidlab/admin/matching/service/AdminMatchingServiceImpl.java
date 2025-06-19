package kernel.maidlab.admin.matching.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import kernel.maidlab.admin.matching.repository.AdminMatchingRepository;
import kernel.maidlab.common.dto.matching.response.MatchingResponseDto;
import kernel.maidlab.common.entity.matching.Matching;
import kernel.maidlab.common.enums.Status;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminMatchingServiceImpl implements AdminMatchingService {

	private final AdminMatchingRepository adminMatchingRepository;

	@Override
	public List<MatchingResponseDto> allMatching(HttpServletRequest request, int page, int size) {
		Pageable pageable = PageRequest.of(page, size);
		Page<Matching> matchings = adminMatchingRepository.findAll(pageable);
		return matchings.stream()
			.map(matching -> MatchingResponseDto.builder()
				.reservationId(matching.getReservationId())
				.managerId(matching.getManagerId())
				.matchingStatus(matching.getMatchingStatus())
				.build())
			.toList();
	}

	@Override
	public List<MatchingResponseDto> statusMatching(Status status, int page, int size) {
		Page<Matching> matchings;
		Pageable pageable = PageRequest.of(page, size);
		matchings = adminMatchingRepository.findAllByMatchingStatus(status, pageable);
		return matchings.stream()
			.map(matching -> MatchingResponseDto.builder()
				.reservationId(matching.getReservationId())
				.managerId(matching.getManagerId())
				.matchingStatus(matching.getMatchingStatus())
				.build())
			.toList();
	}

	@Transactional
	@Override
	public void changeManager(Long reservationId, Long managerId) {
		Matching matching = adminMatchingRepository.findByReservationId(reservationId);
		matching.setManagerId(managerId);
		matching.setMatchingStatus(Status.PENDING);
	}

}
