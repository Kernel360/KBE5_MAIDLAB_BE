package kernel.maidlab.domain.reservation.repository;

import kernel.maidlab.common.enums.Status;
import kernel.maidlab.common.enums.UserType;
import kernel.maidlab.domain.reservation.dto.response.ReservationDetailResponseDto;
import kernel.maidlab.domain.reservation.dto.response.ReservationResponseDto;
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
