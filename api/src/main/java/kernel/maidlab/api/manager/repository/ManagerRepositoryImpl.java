package kernel.maidlab.api.manager.repository;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;

import jakarta.persistence.EntityManager;
import kernel.maidlab.api.auth.entity.Manager;
import kernel.maidlab.api.auth.entity.QManager;
import kernel.maidlab.api.manager.entity.QManagerRegion;
import kernel.maidlab.api.manager.entity.QRegion;
import kernel.maidlab.api.manager.entity.QManagerSchedule;
import kernel.maidlab.api.matching.dto.AvailableManagerResponseDto;

@Repository
public class ManagerRepositoryImpl implements ManagerRepositoryCustom {

	private final JPAQueryFactory QueryFactory;
	// private final ManagerService managerService;

	public ManagerRepositoryImpl(EntityManager em) {
		this.QueryFactory = new JPAQueryFactory(em);
		// this.managerService = managerService;
	}

	@Override
	public List<AvailableManagerResponseDto> FindAvailableManagers(String gu, LocalDateTime Start, LocalDateTime End) {
		QManager manager = QManager.manager;
		QManagerRegion managerRegion = QManagerRegion.managerRegion;
		QRegion region = QRegion.region;
		QManagerSchedule managerSchedule = QManagerSchedule.managerSchedule;
		System.out.println("region : " + region.regionName.toString());
		// QReservation reservation = QReservation.reservation;
		DayOfWeek days = Start.getDayOfWeek();
		LocalTime startTime = Start.toLocalTime();
		LocalTime endTime = End.toLocalTime();
		System.out.println("days: " + days);
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
			.join(managerRegion).on(managerRegion.ManagerId.eq(manager.id))
			.join(region).on(managerRegion.regionId.eq(region.id))
			.join(managerSchedule).on(managerSchedule.manager_id.eq(manager.id)) // manager_schedule 조인
			.where(
				region.regionName.eq(gu),
				managerSchedule.starttime.loe(startTime),          // 시작 시간보다 이르거나 같아야 함
				managerSchedule.endtime.goe(endTime),              // 종료 시간보다 늦거나 같아야 함
				managerSchedule.workday.eq(days)                   // 요일 일치
				// manager.isVerified.eq(Manager.VerificationStatus.APPROVED),
				// manager.isDeleted.isFalse(),
				// manager.id.notIn(
				//     JPAExpressions
				//         .select(reservation.MatchManagerId)
				//         .from(reservation)
				//         .where(
				//             reservation.MatchManagerId.isNotNull(),
				//             reservation.status.ne(ReservationStatus.CANCELED)
				//         )
				// )
			)
			.fetch();
	}

}
