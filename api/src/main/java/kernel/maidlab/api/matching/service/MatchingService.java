package kernel.maidlab.api.matching.service;

import java.util.List;

import kernel.maidlab.api.matching.dto.AvailableManagerResponseDto;
import kernel.maidlab.api.matching.dto.MatchingDto;
import kernel.maidlab.api.matching.dto.MatchingRequestDto;
import kernel.maidlab.common.enums.Status;

public interface MatchingService {

	public List<AvailableManagerResponseDto> FindAvailableManagers(MatchingRequestDto dto);

	void createMatching(MatchingDto dto);

	void ChangeStatus(Long reservationId, Status status);
}
