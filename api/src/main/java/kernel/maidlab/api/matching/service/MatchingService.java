package kernel.maidlab.api.matching.service;

import jakarta.servlet.http.HttpServletRequest;
import kernel.maidlab.api.consumer.dto.response.LikedManagerResponseDto;
import kernel.maidlab.api.consumer.entity.Consumer;
import kernel.maidlab.api.matching.dto.request.MatchingRequestDto;
import kernel.maidlab.api.matching.dto.response.AvailableManagerResponseDto;
import kernel.maidlab.api.matching.dto.response.MatchingResponseDto;
import kernel.maidlab.api.matching.dto.response.RequestMatchingListResponseDto;
import kernel.maidlab.common.enums.Status;

import java.util.List;

public interface MatchingService {

	List<AvailableManagerResponseDto> findAvailableManagers(MatchingRequestDto dto);

	void createMatching(MatchingResponseDto dto);

	void changeStatus(Long reservationId, Status status);

	List<RequestMatchingListResponseDto> myMatching(HttpServletRequest request, int page, int size);

	List<LikedManagerResponseDto> preferenceManager(HttpServletRequest request);

	List<AvailableManagerResponseDto> previousManager(Consumer consumer);
}
