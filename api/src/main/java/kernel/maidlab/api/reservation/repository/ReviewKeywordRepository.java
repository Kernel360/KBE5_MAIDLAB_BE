package kernel.maidlab.api.reservation.repository;

import kernel.maidlab.api.reservation.entity.ReviewKeyword;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewKeywordRepository extends JpaRepository<ReviewKeyword, Long> {
}
