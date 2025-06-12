package kernel.maidlab.api.reservation.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import kernel.maidlab.api.reservation.entity.Reservation;
import kernel.maidlab.api.reservation.entity.Review;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

	@Query(value = """
        SELECT
            r.id as reviewId,
            r.rating as rating,
            c.name as consumerName,
            r.comment as comment,
            sdt.service_type as serviceType,
            sdt.service_detail_type as serviceDetailType
        FROM review r
        JOIN consumer c ON r.consumer_id = c.id
        JOIN service_detail_type sdt ON r.service_detail_type_id = sdt.id
        WHERE r.manager_id = :managerId
        AND r.is_consumer_to_manager = true
        ORDER BY r.review_date DESC
        """, nativeQuery = true)
	List<Object[]> findManagerReviewDetails(@Param("managerId") Long managerId);
	Boolean existsReviewsByReservationId(Long reservationId);
}
