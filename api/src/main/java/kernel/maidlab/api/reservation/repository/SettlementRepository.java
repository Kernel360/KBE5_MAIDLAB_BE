package kernel.maidlab.api.reservation.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import kernel.maidlab.api.reservation.entity.Settlement;

public interface SettlementRepository extends JpaRepository<Settlement, Long> {
	List<Settlement> findByManagerIdAndCreatedAtBetween(Long managerId, LocalDateTime start, LocalDateTime end);

	List<Settlement> findAllByCreatedAtBetween(LocalDateTime start, LocalDateTime end);

	Page<Settlement> findByCreatedAtBetween(LocalDateTime start, LocalDateTime end, Pageable pageable);

}
