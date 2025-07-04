package kernel.maidlab.api.reservation.repository;

import java.util.List;
import java.util.Optional;

import kernel.maidlab.common.dto.reservation.response.ReservationDetailResponseDto;
import kernel.maidlab.common.dto.reservation.response.ReservationResponseDto;
import kernel.maidlab.common.enums.Status;
import kernel.maidlab.common.enums.UserType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ReservationRepositoryCustom {
	List<ReservationResponseDto> findAllWithReviewByConsumerId(Long consumerId);

	List<ReservationResponseDto> findAllWithReviewByManagerId(Long managerId);

	ReservationDetailResponseDto findDetailReservationByIdAndUser(Long reservationId, Long userId, UserType userType);

	Page<ReservationResponseDto> findConsumerReservationsWithPaging(Long consumerId, Status status, Pageable pageable);

}
