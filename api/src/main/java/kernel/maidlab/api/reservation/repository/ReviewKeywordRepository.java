package kernel.maidlab.api.reservation.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import kernel.maidlab.common.entity.reservation.ReviewKeyword;

public interface ReviewKeywordRepository extends JpaRepository<ReviewKeyword, Long> {
}
