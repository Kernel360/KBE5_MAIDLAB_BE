package kernel.maidlab.api.matching.service;

import java.util.List;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import kernel.maidlab.api.auth.entity.Manager;
import kernel.maidlab.api.matching.dto.response.AvailableManagerResponseDto;
import kernel.maidlab.api.matching.dto.response.MatchingResponseDto;
import kernel.maidlab.api.matching.dto.request.MatchingRequestDto;
import kernel.maidlab.common.enums.Status;

public interface MatchingService {

	public List<AvailableManagerResponseDto> findAvailableManagers(MatchingRequestDto dto);

	void createMatching(MatchingResponseDto dto);

	void changeStatus(Long reservationId, Status status);

	@Transactional
	void changeManager(Long reservationId, Manager manager);

	List<MatchingResponseDto> allMatching(HttpServletRequest request);
}
