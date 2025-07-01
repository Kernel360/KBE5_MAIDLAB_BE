package kernel.maidlab.api.matching.repository;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import feign.Param;
import kernel.maidlab.common.entity.matching.Matching;
import kernel.maidlab.common.enums.Status;

public interface MatchingRepository extends JpaRepository<Matching, Long> {
	boolean existsByReservationId(Long ReservationId);
	Matching findByReservationId(Long ReservationId);
	Page<Matching> findByManagerId(Long ManagerId, Pageable pageable);
	Page<Matching> findAllByMatchingStatus(Status MatchingStatus, Pageable pageable);

	@Modifying
	@Query("UPDATE Matching m SET m.matchingStatus = :rejectedStatus " +
		"WHERE m.matchingStatus = :pendingStatus AND m.updatedAt < :expiredTime")
	int bulkExpirePendingMatching(
		@Param("rejectedStatus") Status rejectedStatus,
		@Param("pendingStatus") Status pendingStatus,
		@Param("expiredTime") LocalDateTime expiredTime
	);
}
