package kernel.maidlab.domain.reservation.repository;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import kernel.maidlab.domain.reservation.entity.Reservation;

public interface ReservationRepository extends JpaRepository<Reservation, Long>, ReservationRepositoryCustom {
	Page<Reservation> findAllByReservationDateBetween(LocalDateTime start, LocalDateTime end, Pageable pageable);
}
