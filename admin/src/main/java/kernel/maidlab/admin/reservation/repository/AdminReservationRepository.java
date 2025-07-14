package kernel.maidlab.admin.reservation.repository;

import java.time.LocalDateTime;
import java.util.Collection;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import kernel.maidlab.common.entity.reservation.Reservation;
import kernel.maidlab.common.enums.Status;

public interface AdminReservationRepository extends JpaRepository<Reservation, Long>, AdminReservationRepositoryCustom {
	Page<Reservation> findAllByReservationDateBetween(LocalDateTime start, LocalDateTime end, Pageable pageable);

	Page<Reservation> findAllByConsumerId(Long consumerId, Pageable pageable);

	Page<Reservation> findAllByManagerId(Long managerId, Pageable pageable);

	Long countByConsumerId(Long consumerId);

	Long countByConsumerIdAndStatus(Long consumerId, Status status);

	Long countByManagerIdAndStatusIn(Long managerId, Collection<Status> statuses);

	Long countByManagerIdAndStatus(Long managerId, Status status);
}
