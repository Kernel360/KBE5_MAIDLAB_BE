package kernel.maidlab.admin.reservation.repository;

import kernel.maidlab.domain.reservation.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;


public interface AdminReviewRepository extends JpaRepository<Review, Long> {
	Long countByConsumerIdAndIsConsumerToManager(Long consumerId, boolean b);

	Long countByManagerIdAndIsConsumerToManager(Long managerId, boolean b);
}
