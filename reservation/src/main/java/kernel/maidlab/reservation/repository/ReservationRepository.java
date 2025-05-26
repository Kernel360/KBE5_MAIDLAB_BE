package kernel.maidlab.reservation.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import kernel.maidlab.reservation.entity.Reservation;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

	// List<Reservation> findByConsumerId(Long consumerId);
	// List<Reservation> findByMatchManagerId(Long managerId);
	// List<Reservation> findByReservationDateBetween(LocalDateTime start, LocalDateTime end);
	// List<Reservation> findByStatus(ReservationStatus status);
}
