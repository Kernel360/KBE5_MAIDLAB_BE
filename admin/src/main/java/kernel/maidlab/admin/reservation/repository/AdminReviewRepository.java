package kernel.maidlab.admin.reservation.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import kernel.maidlab.domain.reservation.entity.Review;

public interface AdminReviewRepository extends JpaRepository<Review, Long> {
	Long countByConsumerIdAndIsConsumerToManager(Long consumerId, boolean b);

	Long countByManagerIdAndIsConsumerToManager(Long managerId, boolean b);
}
