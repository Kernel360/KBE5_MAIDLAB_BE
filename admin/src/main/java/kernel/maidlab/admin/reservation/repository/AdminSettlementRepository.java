package kernel.maidlab.admin.reservation.repository;

import kernel.maidlab.api.reservation.entity.Settlement;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;


public interface AdminSettlementRepository extends JpaRepository<Settlement, Long> {
	List<Settlement> findByManagerIdAndCreatedAtBetween(Long managerId, LocalDateTime start, LocalDateTime end);

	List<Settlement> findAllByCreatedAtBetween(LocalDateTime start, LocalDateTime end);

	Page<Settlement> findByCreatedAtBetween(LocalDateTime start, LocalDateTime end, Pageable pageable);

	@Query("SELECT COALESCE(SUM(s.amount), 0) FROM Settlement s WHERE s.managerId = :managerId AND s.status = 'APPROVED'")
	BigDecimal sumAmountByManagerId(@Param("managerId") Long managerId);

}
