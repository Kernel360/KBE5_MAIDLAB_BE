package kernel.maidlab.domain.reservation.repository;

import kernel.maidlab.domain.reservation.entity.Reservation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public interface ReservationRepository extends JpaRepository<Reservation, Long>, ReservationRepositoryCustom {
    Page<Reservation> findAllByReservationDateBetween(LocalDateTime start, LocalDateTime end, Pageable pageable);
}
