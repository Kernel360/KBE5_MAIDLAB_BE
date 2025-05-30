package kernel.maidlab.api.matching.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import kernel.maidlab.api.matching.entity.Matching;

public interface MatchingRepository extends JpaRepository<Matching, Long> {
	boolean existsByReservationId(Long ReservationId);
	Matching findByReservationId(Long ReservationId);
}
