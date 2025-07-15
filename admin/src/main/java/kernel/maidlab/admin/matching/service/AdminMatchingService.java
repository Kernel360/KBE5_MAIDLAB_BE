package kernel.maidlab.admin.matching.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import kernel.maidlab.api.matching.dto.response.MatchingResponseDto;
import kernel.maidlab.common.enums.Status;

import java.util.List;

public interface AdminMatchingService {
	List<MatchingResponseDto> allMatching(HttpServletRequest request, int page, int size);

	List<MatchingResponseDto> statusMatching(Status status, int page, int size);

	@Transactional
	void changeManager(Long reservationId, Long managerId);
}
