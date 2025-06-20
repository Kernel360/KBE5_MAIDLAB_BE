package kernel.maidlab.api.manager.repository;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;

import kernel.maidlab.common.dto.matching.response.AvailableManagerResponseDto;

import static kernel.maidlab.common.entity.manager.QManager.manager;
import static kernel.maidlab.common.entity.manager.QManagerRegion.managerRegion;
import static kernel.maidlab.common.entity.manager.QManagerSchedule.managerSchedule;
import static kernel.maidlab.common.entity.manager.QRegion.region;
import static kernel.maidlab.common.entity.reservation.QReservation.reservation;

import kernel.maidlab.common.enums.Status;
import lombok.RequiredArgsConstructor;

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
					manager.introduceText))
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

}
