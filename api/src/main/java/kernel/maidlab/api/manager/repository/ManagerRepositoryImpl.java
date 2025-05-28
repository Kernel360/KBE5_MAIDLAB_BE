package kernel.maidlab.api.manager.repository;

import static kernel.maidlab.api.manager.entity.QManagerSchedule.*;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;

import jakarta.persistence.EntityManager;
import kernel.maidlab.api.manager.entity.QManager;
import kernel.maidlab.api.manager.entity.QManagerSite;
import kernel.maidlab.api.manager.entity.QSite;
import kernel.maidlab.api.matching.dto.ManagerResponseDto;

@Repository
public class ManagerRepositoryImpl implements ManagerRepositoryCustom {

	private final JPAQueryFactory QueryFactory;

	public ManagerRepositoryImpl(EntityManager em) {
		this.QueryFactory = new JPAQueryFactory(em);
	}

	@Override
	public List<ManagerResponseDto> FindAvailableManagers(String gu, LocalDateTime Start, LocalDateTime End) {
		QManager manager = QManager.manager;
		QManagerSite managerSite = QManagerSite.managerSite;
		QSite site = QSite.site;
		// QReservation reservation = QReservation.reservation;
		DayOfWeek days = Start.getDayOfWeek();
		LocalTime startTime = Start.toLocalTime();
		LocalTime endTime = End.toLocalTime();
		System.out.println("days: " + days);
		return QueryFactory
			.select(Projections.constructor(
				ManagerResponseDto.class,
				manager.id,
				manager.name
			))
			.from(manager)
			.join(managerSite).on(managerSite.ManagerId.eq(manager.id))
			.join(site).on(managerSite.SiteId.eq(site.id))
			.join(managerSchedule).on(managerSchedule.manager_id.eq(manager.id)) // manager_schedule 조인
			.where(
				site.gu.eq(gu),
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
