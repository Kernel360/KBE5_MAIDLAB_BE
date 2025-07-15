package kernel.maidlab.domain.reservation.repository;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kernel.maidlab.domain.manager.entity.QManager;
import kernel.maidlab.domain.manager.entity.QManagerRegion;
import kernel.maidlab.domain.manager.entity.QRegion;
import kernel.maidlab.domain.reservation.dto.response.ReservationDetailResponseDto;
import kernel.maidlab.domain.reservation.dto.response.ReservationResponseDto;
import kernel.maidlab.domain.reservation.entity.QReservation;
import kernel.maidlab.domain.reservation.entity.QReview;
import kernel.maidlab.domain.reservation.entity.QServiceDetailType;
import kernel.maidlab.common.enums.ResponseType;
import kernel.maidlab.common.enums.Status;
import kernel.maidlab.common.enums.UserType;
import kernel.maidlab.core.exception.custom.ReservationException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class ReservationRepositoryCustomImpl implements ReservationRepositoryCustom {

	private final JPAQueryFactory queryFactory;

	QReservation reservation = QReservation.reservation;
	QServiceDetailType serviceDetailType = QServiceDetailType.serviceDetailType1;
	QReview review = QReview.review;
	QManager manager = QManager.manager;
	QManagerRegion managerRegion = QManagerRegion.managerRegion;
	QRegion region = QRegion.region;

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
	public ReservationDetailResponseDto findDetailReservationByIdAndUser(Long reservationId, Long userId,
																		 UserType userType) {

		// 사용자 일치 조건 (권한 체크)
		BooleanExpression userCondition = (userType == UserType.MANAGER)
			? reservation.managerId.eq(userId)
			: reservation.consumerId.eq(userId);

		// 방어코드: userCondition null일 수 있으므로
		if (userCondition == null) {
			throw new ReservationException(ResponseType.THIS_USER_DOES_NOT_EXIST);
		}

		List<Tuple> tuples = queryFactory
			.select(
				reservation.id,
				reservation.status,
				serviceDetailType.serviceType.stringValue(),
				serviceDetailType.serviceDetailType,
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
				reservation.finalPaymentPrice,
				region.regionName
			)
			.from(reservation)
			.join(reservation.serviceDetailType, serviceDetailType)
			.join(manager).on(manager.id.eq(reservation.managerId))
			.leftJoin(managerRegion).on(managerRegion.manager.id.eq(manager.id))
			.leftJoin(region).on(region.id.eq(managerRegion.regionId.id))
			.where(reservation.id.eq(reservationId).and(userCondition))
			.fetch();

		if (tuples.isEmpty()) {
			throw new ReservationException(ResponseType.THIS_USER_DOES_NOT_EXIST);
		}

		Tuple first = tuples.getFirst();
		List<String> regionNames = tuples.stream()
			.map(t -> t.get(region.regionName))
			.distinct()
			.collect(Collectors.toList());

		return ReservationDetailResponseDto.builder()
			.status(first.get(reservation.status))
			.serviceType(first.get(serviceDetailType.serviceType.stringValue()))
			.serviceDetailType(first.get(serviceDetailType.serviceDetailType))
			.address(first.get(reservation.address))
			.addressDetail(first.get(reservation.addressDetail))
			.managerUuid(first.get(manager.uuid))
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
			.finalPaymentPrice(first.get(reservation.finalPaymentPrice))
			.build();
	}

	@Override
	public Page<ReservationResponseDto> findConsumerReservationsWithPaging(Long consumerId, Status status,
		Pageable pageable) {
		BooleanExpression baseCondition = reservation.consumerId.eq(consumerId);

		BooleanExpression statusCondition = null;
		BooleanExpression dateCondition = null;

		// 상태별 조건 처리
		if (status != null) {
			if (status == Status.PAID) {
				// PAID 상태일 때는 WORKING 상태도 포함하고 WORKING이 우선순위가 높음
				statusCondition = reservation.status.eq(Status.PAID).or(reservation.status.eq(Status.WORKING));
			} else {
				statusCondition = reservation.status.eq(status);
			}

			// MATCHED, PAID, PENDING 상태일 때는 오늘 날짜 이후의 예약만 조회
			if (status == Status.MATCHED || status == Status.PAID || status == Status.PENDING) {
				LocalDate today = LocalDate.now();
				LocalDateTime startOfToday = today.atStartOfDay();
				dateCondition = reservation.reservationDate.goe(startOfToday);
			}
		}

		// 조건들을 결합
		BooleanExpression finalCondition = baseCondition;
		if (statusCondition != null) {
			finalCondition = finalCondition.and(statusCondition);
		}
		if (dateCondition != null) {
			finalCondition = finalCondition.and(dateCondition);
		}

		// Pageable의 Sort 정보를 QueryDSL OrderSpecifier로 변환
		List<OrderSpecifier<?>> orderSpecifiers = new ArrayList<>();

		// PAID 상태일 때 WORKING 상태가 우선순위가 높게 정렬
		if (status == Status.PAID) {
			NumberExpression<Integer> statusPriority = new CaseBuilder()
				.when(reservation.status.eq(Status.WORKING)).then(0)
				.when(reservation.status.eq(Status.PAID)).then(1)
				.otherwise(2);
			orderSpecifiers.add(new OrderSpecifier<>(Order.ASC, statusPriority));
		}

		if (pageable.getSort().isSorted()) {
			for (org.springframework.data.domain.Sort.Order sortOrder : pageable.getSort()) {
				Order direction = sortOrder.isAscending() ? Order.ASC : Order.DESC;
				switch (sortOrder.getProperty()) {
					case "createdAt":
						orderSpecifiers.add(new OrderSpecifier<>(direction, reservation.createdAt));
						break;
					case "reservationDate":
						orderSpecifiers.add(new OrderSpecifier<>(direction, reservation.reservationDate));
						break;
					case "totalPrice":
						orderSpecifiers.add(new OrderSpecifier<>(direction, reservation.totalPrice));
						break;
					case "startTime":
						orderSpecifiers.add(new OrderSpecifier<>(direction, reservation.startTime));
						break;
					default:
						orderSpecifiers.add(new OrderSpecifier<>(direction, reservation.createdAt));
						break;
				}
			}
		} else {
			// 기본 정렬: createdAt DESC
			orderSpecifiers.add(new OrderSpecifier<>(Order.DESC, reservation.createdAt));
		}

		JPAQuery<ReservationResponseDto> query = queryFactory
			.select(Projections.constructor(ReservationResponseDto.class,
				reservation.id,
				reservation.status,
				reservation.serviceDetailType.serviceType.stringValue(),
				reservation.serviceDetailType.serviceDetailType,
				reservation.reservationDate.stringValue(),
				reservation.startTime.stringValue().substring(11, 16),
				reservation.endTime.stringValue().substring(11, 16),
				JPAExpressions.selectOne().from(review).where(review.reservationId.eq(reservation.id)).exists(),
				reservation.totalPrice))
			.from(reservation)
			.join(reservation.serviceDetailType, serviceDetailType)
			.where(finalCondition)
			.orderBy(orderSpecifiers.toArray(new OrderSpecifier[0]));

		Long totalCount = queryFactory
			.select(reservation.count())
			.from(reservation)
			.join(reservation.serviceDetailType, serviceDetailType)
			.where(finalCondition)
			.fetchOne();

		long total = totalCount != null ? totalCount : 0L;

		List<ReservationResponseDto> content = query
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();

		return new PageImpl<>(content, pageable, total);
	}

	@Override
	public Page<ReservationResponseDto> getManagerReservationsWithPaging(Long managerId, String status,
		Pageable pageable) {
		BooleanExpression baseCondition = reservation.managerId.eq(managerId);
		BooleanExpression statusCondition = null;

		// 상태별 조건 처리
		if ("TODAY".equals(status)) {
			// 오늘 날짜 조건 (LocalDate와 LocalDateTime 비교를 위해 날짜 범위로 조건 생성)
			LocalDate today = LocalDate.now();
			LocalDateTime startOfDay = today.atStartOfDay();
			LocalDateTime endOfDay = today.atTime(23, 59, 59);
			statusCondition = reservation.reservationDate.goe(startOfDay)
				.and(reservation.reservationDate.loe(endOfDay));
		} else if ("PAID".equals(status)) {
			// PAID와 MATCHED 상태 함께 조회, 오늘 날짜 이상만 조회
			LocalDate today = LocalDate.now();
			LocalDateTime startOfToday = today.atStartOfDay();
			statusCondition = (reservation.status.eq(Status.PAID).or(reservation.status.eq(Status.MATCHED)))
				.and(reservation.reservationDate.goe(startOfToday));
		} else if ("WORKING".equals(status)) {
			statusCondition = reservation.status.eq(Status.WORKING);
		} else if ("COMPLETED".equals(status)) {
			statusCondition = reservation.status.eq(Status.COMPLETED);
		}

		BooleanExpression finalCondition = statusCondition != null ? baseCondition.and(statusCondition) : baseCondition;

		// 정렬 조건 처리
		List<OrderSpecifier<?>> orderSpecifiers = new ArrayList<>();

		if ("TODAY".equals(status)) {
			// TODAY 요청시 상태별 우선순위 정렬 (WORKING > PAID > 나머지)
			NumberExpression<Integer> statusPriority = new CaseBuilder()
				.when(reservation.status.eq(Status.WORKING)).then(0)
				.when(reservation.status.eq(Status.PAID)).then(1)
				.otherwise(2);
			orderSpecifiers.add(new OrderSpecifier<>(Order.ASC, statusPriority));
		}

		// reservationDate 기준 정렬
		Order direction = pageable.getSort().isSorted() &&
			pageable.getSort().getOrderFor("reservationDate") != null &&
			pageable.getSort().getOrderFor("reservationDate").isAscending() ? Order.ASC : Order.DESC;

		orderSpecifiers.add(new OrderSpecifier<>(direction, reservation.reservationDate));

		// PAID 상태일 때는 시간까지 고려한 정렬
		if ("PAID".equals(status)) {
			orderSpecifiers.add(new OrderSpecifier<>(direction, reservation.startTime));
		}

		JPAQuery<ReservationResponseDto> query = queryFactory
			.select(Projections.constructor(ReservationResponseDto.class,
				reservation.id,
				reservation.status,
				reservation.serviceDetailType.serviceType.stringValue(),
				reservation.serviceDetailType.serviceDetailType,
				reservation.reservationDate.stringValue(),
				reservation.startTime.stringValue().substring(11, 16),
				reservation.endTime.stringValue().substring(11, 16),
				JPAExpressions.selectOne().from(review).where(review.reservationId.eq(reservation.id)).exists(),
				reservation.totalPrice))
			.from(reservation)
			.join(reservation.serviceDetailType, serviceDetailType)
			.where(finalCondition)
			.orderBy(orderSpecifiers.toArray(new OrderSpecifier[0]));

		Long totalCount = queryFactory
			.select(reservation.count())
			.from(reservation)
			.join(reservation.serviceDetailType, serviceDetailType)
			.where(finalCondition)
			.fetchOne();

		long total = totalCount != null ? totalCount : 0L;

		List<ReservationResponseDto> content = query
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();

		return new PageImpl<>(content, pageable, total);
	}

}
