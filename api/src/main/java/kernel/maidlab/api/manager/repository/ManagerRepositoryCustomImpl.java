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

import jakarta.persistence.EntityManager;
import kernel.maidlab.api.auth.entity.QManager;
import kernel.maidlab.api.manager.entity.QManagerRegion;
import kernel.maidlab.api.manager.entity.QRegion;
import kernel.maidlab.api.manager.entity.QManagerSchedule;
import kernel.maidlab.api.reservation.entity.QReservation;
import kernel.maidlab.api.matching.dto.response.AvailableManagerResponseDto;
import kernel.maidlab.common.enums.Status;

@Repository
public class ManagerRepositoryCustomImpl implements ManagerRepositoryCustom {

	private final JPAQueryFactory QueryFactory;
	// private final ManagerService managerService;

	public ManagerRepositoryCustomImpl(EntityManager em) {
		this.QueryFactory = new JPAQueryFactory(em);
		// this.managerService = managerService;
	}

	@Override
	public List<AvailableManagerResponseDto> findAvailableManagers(String gu, LocalDateTime start, LocalDateTime end) {
		QManager manager = QManager.manager;
		QManagerRegion managerRegion = QManagerRegion.managerRegion;
		QRegion region = QRegion.region;
		QManagerSchedule managerSchedule = QManagerSchedule.managerSchedule;
		System.out.println("region : " + region.regionName.toString());
		QReservation reservation = QReservation.reservation;
		DayOfWeek days = start.getDayOfWeek();
		LocalTime startTime = start.toLocalTime();
		LocalTime endTime = end.toLocalTime();
		System.out.println("days: " + days);
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
		// String uuid = QueryFactory
		// 	.select(manager.uuid)
		// 	.from(manager)
		// 	.fetch();
		//System.out.println("uuid :" + uuid);
		// System.out.println("id:" + managerService.GetIdByUuid(uuid));
		return QueryFactory
			.select(Projections.constructor(
				AvailableManagerResponseDto.class,
				manager.uuid,
				manager.name
			))
			.from(manager)
			.join(managerRegion).on(managerRegion.manager.id.eq(manager.id))
			.join(region).on(managerRegion.regionId.id.eq(region.id))
			.join(managerSchedule).on(managerSchedule.manager.id.eq(manager.id)) // manager_schedule 조인
			.where(
				region.regionName.eq(gu),
				managerSchedule.availableStartTime.loe(startTime.format(formatter)),          // 시작 시간보다 이르거나 같아야 함
				managerSchedule.availableEndTime.goe(endTime.format(formatter)),              // 종료 시간보다 늦거나 같아야 함
				managerSchedule.availableDay.eq(days.toString()),                  // 요일 일치
				manager.isVerified.eq(Status.APPROVED),
				manager.isDeleted.isFalse(),
				manager.id.notIn(
					JPAExpressions
						.select(reservation.managerId)
						.from(reservation)
						.where(
							reservation.managerId.isNotNull(),
							reservation.status.eq(Status.APPROVED),
							reservation.startTime.lt(end),     // 예약 시작 < 요청 종료
							reservation.endTime.gt(start)      // 예약 종료 > 요청 시작
						)
				)
			)
			.fetch();
	}

}
