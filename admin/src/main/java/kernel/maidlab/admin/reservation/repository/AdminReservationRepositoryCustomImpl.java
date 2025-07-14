package kernel.maidlab.admin.reservation.repository;

import static kernel.maidlab.common.entity.reservation.QReservation.*;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;

import kernel.maidlab.common.enums.Status;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class AdminReservationRepositoryCustomImpl implements AdminReservationRepositoryCustom {

	private final JPAQueryFactory queryFactory;

	@Override
	public Long countByReservationDate(LocalDate today) {
		return queryFactory
			.select(reservation.count())
			.from(reservation)
			.where(
				reservation.reservationDate.goe(today.atStartOfDay()),
				reservation.reservationDate.lt(today.plusDays(1).atStartOfDay())
			)
			.fetchOne();
	}

	@Override
	public BigDecimal sumTotalPrice(Long consumerId) {
		return queryFactory
			.select(reservation.totalPrice.sum())
			.from(reservation)
			.where(
				reservation.consumerId.eq(consumerId),
				reservation.status.in(Status.COMPLETED, Status.WORKING)
			)
			.fetchOne();
	}

}
