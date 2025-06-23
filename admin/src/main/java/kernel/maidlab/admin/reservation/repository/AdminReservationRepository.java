package kernel.maidlab.admin.reservation.repository;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import kernel.maidlab.common.entity.reservation.Reservation;

public interface AdminReservationRepository extends JpaRepository<Reservation, Long>, AdminReservationRepositoryCustom {
	Page<Reservation> findAllByReservationDateBetween(LocalDateTime start, LocalDateTime end, Pageable pageable);

}
