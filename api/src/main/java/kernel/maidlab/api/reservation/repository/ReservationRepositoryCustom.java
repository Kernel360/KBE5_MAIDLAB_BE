package kernel.maidlab.api.reservation.repository;

import java.util.List;

import kernel.maidlab.common.dto.reservation.response.ReservationResponseDto;

public interface ReservationRepositoryCustom {
	List<ReservationResponseDto> findAllWithReviewByConsumerId(Long consumerId);

	List<ReservationResponseDto> findAllWithReviewByManagerId(Long managerId);

}
