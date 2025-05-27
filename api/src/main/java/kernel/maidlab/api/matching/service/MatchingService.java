package kernel.maidlab.api.matching.service;

import java.util.List;

import kernel.maidlab.api.matching.dto.ManagerResponseDto;
import kernel.maidlab.api.matching.dto.MatchingRequestDto;

public interface MatchingService {

	public List<ManagerResponseDto> FindAvailableManagers(MatchingRequestDto dto);
}
