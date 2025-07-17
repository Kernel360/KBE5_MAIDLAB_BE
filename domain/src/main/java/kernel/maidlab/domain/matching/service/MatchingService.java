package kernel.maidlab.domain.matching.service;

import jakarta.servlet.http.HttpServletRequest;
import kernel.maidlab.common.enums.Status;
import kernel.maidlab.domain.consumer.dto.response.LikedManagerResponseDto;
import kernel.maidlab.domain.consumer.entity.Consumer;
import kernel.maidlab.domain.matching.dto.request.MatchingRequestDto;
import kernel.maidlab.domain.matching.dto.response.AvailableManagerResponseDto;
import kernel.maidlab.domain.matching.dto.response.MatchingResponseDto;
import kernel.maidlab.domain.matching.dto.response.RequestMatchingListResponseDto;

import java.util.List;

public interface MatchingService {

    List<AvailableManagerResponseDto> findAvailableManagers(MatchingRequestDto dto);

    void createMatching(MatchingResponseDto dto);

    void changeStatus(Long reservationId, Status status);

    List<RequestMatchingListResponseDto> myMatching(HttpServletRequest request, int page, int size);

    List<LikedManagerResponseDto> preferenceManager(HttpServletRequest request);

    List<AvailableManagerResponseDto> previousManager(Consumer consumer);
}
