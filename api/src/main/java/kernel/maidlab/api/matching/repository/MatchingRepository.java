package kernel.maidlab.api.matching.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import kernel.maidlab.api.matching.entity.Matching;

public interface MatchingRepository extends JpaRepository<Matching, Long> {
	boolean existsByReservationId(Long ReservationId);
	Matching findByReservationId(Long ReservationId);
	List<Matching> findByManagerId(Long ManagerId);
}
