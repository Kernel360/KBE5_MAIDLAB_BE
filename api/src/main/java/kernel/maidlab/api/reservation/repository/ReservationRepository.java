package kernel.maidlab.api.reservation.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import kernel.maidlab.common.entity.reservation.Reservation;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

	List<Reservation> findByConsumerId(Long consumerId);

	List<Reservation> findByManagerId(Long managerId);

	Page<Reservation> findAllByReservationDateBetween(LocalDateTime start, LocalDateTime end, Pageable pageable);
	// List<Reservation> findByReservationDateBetween(LocalDateTime start, LocalDateTime end);
	// List<Reservation> findByStatus(ReservationStatus status);
}
