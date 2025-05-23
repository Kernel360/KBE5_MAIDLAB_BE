package kernel.maidlab.reservation.repository;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import kernel.maidlab.reservation.entity.Reservation;
import kernel.maidlab.reservation.enums.ReservationStatus;

public interface ReservationRepository extends JpaRepository<Reservation,Long>{

	List<Reservation> findByConsumerId(Long consumerId);
	List<Reservation> findByManagerId(Long managerId);
	List<Reservation> findByReservationDateBetween(LocalDateTime start, LocalDateTime end);
	List<Reservation> findByStatus(ReservationStatus status);
}
