package kernel.maidlab.domain.matching.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import feign.Param;
import kernel.maidlab.common.enums.Status;
import kernel.maidlab.domain.matching.entity.Matching;

public interface MatchingRepository extends JpaRepository<Matching, Long> {
	boolean existsByReservationId(Long ReservationId);

	Matching findByReservationId(Long ReservationId);

	Page<Matching> findByManagerIdAndMatchingStatus(Long id, Status status, Pageable pageable);

	@Modifying
	@Query("UPDATE Matching m SET m.matchingStatus = :rejectedStatus " +
		"WHERE m.matchingStatus = :pendingStatus AND m.updatedAt < :expiredTime")
	int bulkExpirePendingMatching(
		@Param("rejectedStatus") Status rejectedStatus,
		@Param("pendingStatus") Status pendingStatus,
		@Param("expiredTime") LocalDateTime expiredTime
	);

	List<Matching> findByMatchingStatusAndUpdatedAtBefore(Status status, LocalDateTime expiredTime);

	List<Matching> findByMatchingCountGreaterThanEqualOrderByUpdatedAtDesc(int i);
}
