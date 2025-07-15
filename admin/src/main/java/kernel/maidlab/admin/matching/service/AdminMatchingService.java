package kernel.maidlab.admin.matching.service;

import java.util.List;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import kernel.maidlab.common.enums.Status;
import kernel.maidlab.domain.matching.dto.response.MatchingResponseDto;

public interface AdminMatchingService {
	List<MatchingResponseDto> allMatching(HttpServletRequest request, int page, int size);

	List<MatchingResponseDto> statusMatching(Status status, int page, int size);

	@Transactional
	void changeManager(Long reservationId, Long managerId);
}
