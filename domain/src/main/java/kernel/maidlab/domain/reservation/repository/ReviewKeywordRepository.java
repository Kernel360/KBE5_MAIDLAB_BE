package kernel.maidlab.domain.reservation.repository;

import kernel.maidlab.domain.reservation.entity.ReviewKeyword;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewKeywordRepository extends JpaRepository<ReviewKeyword, Long> {
}
