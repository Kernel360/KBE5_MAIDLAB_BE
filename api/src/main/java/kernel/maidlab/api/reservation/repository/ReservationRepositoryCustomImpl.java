package kernel.maidlab.api.reservation.repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;

import kernel.maidlab.common.dto.reservation.response.ReservationDetailResponseDto;
import kernel.maidlab.common.dto.reservation.response.ReservationResponseDto;
import kernel.maidlab.common.entity.manager.QManager;
import kernel.maidlab.common.entity.manager.QManagerRegion;
import kernel.maidlab.common.entity.manager.QRegion;
import kernel.maidlab.common.entity.reservation.QReservation;
import kernel.maidlab.common.entity.reservation.QReview;
import kernel.maidlab.common.entity.reservation.QServiceDetailType;
import kernel.maidlab.common.enums.ResponseType;
import kernel.maidlab.common.enums.UserType;
import kernel.maidlab.common.exception.custom.ReservationException;
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

	@Override
	public ReservationDetailResponseDto findDetailReservationByIdAndUser(Long reservationId, Long userId, UserType userType) {
		QReservation reservation = QReservation.reservation;
		QManager manager = QManager.manager;
		QManagerRegion managerRegion = QManagerRegion.managerRegion;
		QRegion region = QRegion.region;
		QServiceDetailType sdt = QServiceDetailType.serviceDetailType1;

		// 사용자 일치 조건 (권한 체크)
		BooleanExpression userCondition = (userType == UserType.MANAGER)
			? reservation.managerId.eq(userId)
			: reservation.consumerId.eq(userId);

		// 한 번에 가져오기
		List<Tuple> tuples = queryFactory
			.select(
				reservation.id,
				reservation.status,
				sdt.serviceType.stringValue(),
				sdt.serviceDetailType,
				reservation.address,
				reservation.addressDetail,
				manager.uuid,
				manager.name,
				manager.profileImage,
				manager.averageRate,
				manager.phoneNumber,
				reservation.housingType,
				reservation.roomSize,
				reservation.housingInformation,
				reservation.reservationDate,
				reservation.startTime,
				reservation.endTime,
				reservation.serviceAdd,
				reservation.pet,
				reservation.specialRequest,
				reservation.totalPrice,
				region.regionName
			)
			.from(reservation)
			.join(reservation.serviceDetailType, sdt)
			.join(manager).on(manager.id.eq(reservation.managerId))
			.leftJoin(managerRegion).on(managerRegion.manager.id.eq(manager.id))
			.leftJoin(region).on(region.id.eq(managerRegion.regionId.id))
			.where(reservation.id.eq(reservationId).and(userCondition))
			.fetch();

		if (tuples.isEmpty()) {
			throw new ReservationException(ResponseType.DATABASE_ERROR);
		}

		Tuple first = tuples.getFirst();
		List<String> regionNames = tuples.stream()
			.map(t -> t.get(region.regionName))
			.distinct()
			.collect(Collectors.toList());

		return ReservationDetailResponseDto.builder()
				.status(first.get(reservation.status))
				.serviceType(first.get(sdt.serviceType.stringValue()))
				.serviceDetailType(first.get(sdt.serviceDetailType))
				.address(first.get(reservation.address))
				.addressDetail(first.get(reservation.addressDetail))
				.managerUuId(first.get(manager.uuid))
				.managerName(first.get(manager.name))
				.managerProfileImageUrl(first.get(manager.profileImage))
				.managerAverageRate(first.get(manager.averageRate))
				.managerRegion(regionNames)
				.managerPhoneNumber(first.get(manager.phoneNumber))
				.housingType(first.get(reservation.housingType))
				.roomSize(first.get(reservation.roomSize))
				.housingInformation(first.get(reservation.housingInformation))
				.reservationDate(first.get(reservation.reservationDate))
				.startTime(first.get(reservation.startTime))
				.endTime(first.get(reservation.endTime))
				.serviceAdd(first.get(reservation.serviceAdd))
				.pet(first.get(reservation.pet))
				.specialRequest(first.get(reservation.specialRequest))
				.totalPrice(first.get(reservation.totalPrice))
				.build();
	}


}
