package kernel.maidlab.api.matching.service;

import java.util.List;

import jakarta.servlet.http.HttpServletRequest;
import kernel.maidlab.api.matching.dto.response.AvailableManagerResponseDto;
import kernel.maidlab.api.matching.dto.response.MatchingResponseDto;
import kernel.maidlab.api.matching.dto.request.MatchingRequestDto;
import kernel.maidlab.common.enums.Status;

public interface MatchingService {

	List<AvailableManagerResponseDto> findAvailableManagers(MatchingRequestDto dto);

	void createMatching(MatchingResponseDto dto);

	void changeStatus(Long reservationId, Status status);

	void changeManager(Long reservationId, Long managerId);

	List<MatchingResponseDto> allMatching(HttpServletRequest request, int page, int size);

	List<MatchingResponseDto> myMatching(HttpServletRequest request, int page, int size);

	List<MatchingResponseDto> statusMatching(Status status, int page, int size);
}
