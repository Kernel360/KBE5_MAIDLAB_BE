package kernel.maidlab.api.reservation.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import kernel.maidlab.api.reservation.entity.Reservation;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

	List<Reservation> findByConsumerId(Long consumerId);
	List<Reservation> findByManagerId(Long managerId);
	// List<Reservation> findByReservationDateBetween(LocalDateTime start, LocalDateTime end);
	// List<Reservation> findByStatus(ReservationStatus status);
}
