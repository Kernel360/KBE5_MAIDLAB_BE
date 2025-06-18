package kernel.maidlab.api.reservation.repository;

import java.util.List;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;

import kernel.maidlab.common.dto.reservation.response.ReservationResponseDto;
import kernel.maidlab.common.entity.reservation.QReservation;
import kernel.maidlab.common.entity.reservation.QReview;
import kernel.maidlab.common.entity.reservation.QServiceDetailType;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ReservationRepositoryCustomImpl implements ReservationRepositoryCustom {

	private final JPAQueryFactory queryFactory;

	QReservation reservation = QReservation.reservation;
	QServiceDetailType serviceDetailType = QServiceDetailType.serviceDetailType1;
	QReview review = QReview.review;

	@Override
	public List<ReservationResponseDto> findAllWithReviewByConsumerId(Long consumerId) {
		return findAllWithReviewByUserCondition(reservation.consumerId.eq(consumerId));
	}

	@Override
	public List<ReservationResponseDto> findAllWithReviewByManagerId(Long managerId) {
		return findAllWithReviewByUserCondition(reservation.managerId.eq(managerId));
	}

	private List<ReservationResponseDto> findAllWithReviewByUserCondition(BooleanExpression condition) {
		return queryFactory.select(
				Projections.constructor(ReservationResponseDto.class, reservation.id, reservation.status,
					reservation.serviceDetailType.serviceType.stringValue(),
					reservation.serviceDetailType.serviceDetailType,
					reservation.reservationDate.stringValue(),
					reservation.startTime.stringValue().substring(11, 16),
					reservation.endTime.stringValue().substring(11, 16),
					// 리뷰 존재 여부 확인 (EXISTS 서브쿼리)
					JPAExpressions.selectOne().from(review).where(review.reservationId.eq(reservation.id)).exists(),
					reservation.totalPrice))
			.from(reservation)
			.join(reservation.serviceDetailType, serviceDetailType)
			.where(condition)
			.fetch();
	}
}
