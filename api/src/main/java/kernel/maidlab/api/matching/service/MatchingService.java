package kernel.maidlab.api.matching.service;

import java.util.List;

import jakarta.servlet.http.HttpServletRequest;
import kernel.maidlab.common.dto.consumer.response.LikedManagerResponseDto;
import kernel.maidlab.common.dto.matching.request.MatchingRequestDto;
import kernel.maidlab.common.dto.matching.response.AvailableManagerResponseDto;
import kernel.maidlab.common.dto.matching.response.MatchingResponseDto;
import kernel.maidlab.common.dto.matching.response.RequestMatchingListResponseDto;
import kernel.maidlab.common.entity.consumer.Consumer;
import kernel.maidlab.common.enums.Status;

public interface MatchingService {

	List<AvailableManagerResponseDto> findAvailableManagers(MatchingRequestDto dto);

	void createMatching(MatchingResponseDto dto);

	void changeStatus(Long reservationId, Status status);

	List<RequestMatchingListResponseDto> myMatching(HttpServletRequest request, int page, int size);

	List<LikedManagerResponseDto> preferenceManager(HttpServletRequest request);

	List<AvailableManagerResponseDto> previousManager(Consumer consumer);
}
