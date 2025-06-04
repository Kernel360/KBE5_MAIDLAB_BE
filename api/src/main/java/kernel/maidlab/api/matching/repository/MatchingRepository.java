package kernel.maidlab.api.matching.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import kernel.maidlab.api.matching.entity.Matching;
import kernel.maidlab.common.enums.Status;

public interface MatchingRepository extends JpaRepository<Matching, Long> {
	boolean existsByReservationId(Long ReservationId);
	Matching findByReservationId(Long ReservationId);
	Page<Matching> findByManagerId(Long ManagerId, Pageable pageable);
	Page<Matching> findAllByMatchingStatus(Status MatchingStatus, Pageable pageable);

}
