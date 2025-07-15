package kernel.maidlab.domain.reservation.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import kernel.maidlab.domain.reservation.entity.ReviewKeyword;

public interface ReviewKeywordRepository extends JpaRepository<ReviewKeyword, Long> {
}
