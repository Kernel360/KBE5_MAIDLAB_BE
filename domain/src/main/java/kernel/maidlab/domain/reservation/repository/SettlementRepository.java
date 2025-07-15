package kernel.maidlab.domain.reservation.repository;

import kernel.maidlab.domain.reservation.entity.Settlement;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface SettlementRepository extends JpaRepository<Settlement, Long> {
	List<Settlement> findByManagerIdAndCreatedAtBetween(Long managerId, LocalDateTime start, LocalDateTime end);

	List<Settlement> findAllByCreatedAtBetween(LocalDateTime start, LocalDateTime end);

	Page<Settlement> findByCreatedAtBetween(LocalDateTime start, LocalDateTime end, Pageable pageable);

}
