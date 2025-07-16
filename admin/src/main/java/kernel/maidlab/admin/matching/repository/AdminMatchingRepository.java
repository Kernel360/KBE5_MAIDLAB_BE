package kernel.maidlab.admin.matching.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import kernel.maidlab.common.enums.Status;
import kernel.maidlab.domain.matching.entity.Matching;

public interface AdminMatchingRepository extends JpaRepository<Matching, Long> {

	Page<Matching> findAllByMatchingStatusOrderByUpdatedAtDesc(Status status, Pageable pageable);

	Matching findByReservationId(Long reservationId);
}
