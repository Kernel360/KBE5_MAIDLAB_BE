package kernel.maidlab.admin.matching.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import kernel.maidlab.admin.matching.repository.AdminMatchingRepository;
import kernel.maidlab.domain.matching.dto.response.MatchingResponseDto;
import kernel.maidlab.domain.matching.entity.Matching;
import kernel.maidlab.common.enums.Status;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminMatchingServiceImpl implements AdminMatchingService {

	private final AdminMatchingRepository adminMatchingRepository;

	@Override
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
	public void changeManager(Long reservationId, Long managerId) {
		Matching matching = adminMatchingRepository.findByReservationId(reservationId);
		matching.setManagerId(managerId);
		matching.setMatchingStatus(Status.PENDING);
	}

}
