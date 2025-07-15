package kernel.maidlab.domain.manager.repository;


import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kernel.maidlab.domain.consumer.entity.Consumer;
import kernel.maidlab.domain.matching.dto.response.AvailableManagerResponseDto;
import kernel.maidlab.common.enums.Status;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static kernel.maidlab.domain.consumer.entity.QManagerPreference.managerPreference;
import static kernel.maidlab.domain.manager.entity.QManager.manager;
import static kernel.maidlab.domain.manager.entity.QManagerRegion.managerRegion;
import static kernel.maidlab.domain.manager.entity.QManagerSchedule.managerSchedule;
import static kernel.maidlab.domain.manager.entity.QRegion.region;
import static kernel.maidlab.domain.reservation.entity.QReservation.reservation;

@Repository
@RequiredArgsConstructor
public class ManagerRepositoryCustomImpl implements ManagerRepositoryCustom {

	private final JPAQueryFactory QueryFactory;

	@Override
	public List<AvailableManagerResponseDto> findAvailableManagers(String gu, LocalDateTime start, LocalDateTime end) {
		DayOfWeek days = start.getDayOfWeek();
		LocalTime startTime = start.toLocalTime();
		LocalTime endTime = end.toLocalTime();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
		return QueryFactory.select(
				Projections.constructor(AvailableManagerResponseDto.class, manager.uuid, manager.name, manager.averageRate,
					manager.introduceText, manager.profileImage))
			.from(manager)
			.join(managerRegion)
			.on(managerRegion.manager.id.eq(manager.id))
			.join(region)
			.on(managerRegion.regionId.id.eq(region.id))
			.join(managerSchedule)
			.on(managerSchedule.manager.id.eq(manager.id)) // manager_schedule 조인
			.where(region.regionName.eq(gu), managerSchedule.availableStartTime.loe(startTime.format(formatter)),
				// 시작 시간보다 이르거나 같아야 함
				managerSchedule.availableEndTime.goe(endTime.format(formatter)),              // 종료 시간보다 늦거나 같아야 함
				managerSchedule.availableDay.eq(days.toString()),                  // 요일 일치
				manager.isVerified.eq(Status.APPROVED), manager.isDeleted.isFalse(), manager.id.notIn(
					JPAExpressions.select(reservation.managerId)
						.from(reservation)
						.where(reservation.managerId.isNotNull(), reservation.status.eq(Status.APPROVED),
							reservation.startTime.lt(end),     // 예약 시작 < 요청 종료
							reservation.endTime.gt(start)      // 예약 종료 > 요청 시작
						)))
			.fetch();
	}

	@Override
	public List<AvailableManagerResponseDto> previousManagers(Consumer consumer) {
		return QueryFactory.select(
				Projections.constructor(AvailableManagerResponseDto.class, manager.uuid, manager.name, manager.averageRate,
					manager.introduceText, manager.profileImage))
			.from(manager)
			.join(reservation)
			.on(manager.id.eq(reservation.managerId))
			.where(reservation.consumerId.eq(consumer.getId()), reservation.status.eq(Status.COMPLETED),
				manager.id.notIn(
					JPAExpressions.select(managerPreference.manager.id)
						.from(managerPreference)
						.where(managerPreference.consumer.id.eq(consumer.getId()),
							managerPreference.preference.isFalse())
				))
			.fetch();
	}

}
