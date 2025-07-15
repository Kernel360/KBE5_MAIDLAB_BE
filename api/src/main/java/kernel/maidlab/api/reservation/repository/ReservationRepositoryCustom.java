package kernel.maidlab.api.reservation.repository;

import kernel.maidlab.api.reservation.dto.response.ReservationDetailResponseDto;
import kernel.maidlab.api.reservation.dto.response.ReservationResponseDto;
import kernel.maidlab.common.enums.Status;
import kernel.maidlab.common.enums.UserType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ReservationRepositoryCustom {
	List<ReservationResponseDto> findAllWithReviewByConsumerId(Long consumerId);

	List<ReservationResponseDto> findAllWithReviewByManagerId(Long managerId);

	ReservationDetailResponseDto findDetailReservationByIdAndUser(Long reservationId, Long userId, UserType userType);

	Page<ReservationResponseDto> findConsumerReservationsWithPaging(Long consumerId, Status status, Pageable pageable);

	Page<ReservationResponseDto> getManagerReservationsWithPaging(Long managerId, String status, Pageable pageable);

}
