package kernel.maidlab.admin.matching.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import kernel.maidlab.common.entity.matching.Matching;
import kernel.maidlab.common.enums.Status;

public interface AdminMatchingRepository extends JpaRepository<Matching, Long> {

	Page<Matching> findAllByMatchingStatus(Status status, Pageable pageable);

	Matching findByReservationId(Long reservationId);
}
