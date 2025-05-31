package kernel.maidlab.api.reservation.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import kernel.maidlab.api.reservation.entity.Review;

public interface ReviewRepository extends JpaRepository<Review, Long> {
}
