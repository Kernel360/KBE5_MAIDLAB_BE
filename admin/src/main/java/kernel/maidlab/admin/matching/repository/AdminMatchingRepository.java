package kernel.maidlab.admin.matching.repository;

import kernel.maidlab.api.matching.entity.Matching;
import kernel.maidlab.common.enums.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminMatchingRepository extends JpaRepository<Matching, Long> {

	Page<Matching> findAllByMatchingStatusOrderByUpdatedAtDesc(Status status, Pageable pageable);

	Matching findByReservationId(Long reservationId);
}
