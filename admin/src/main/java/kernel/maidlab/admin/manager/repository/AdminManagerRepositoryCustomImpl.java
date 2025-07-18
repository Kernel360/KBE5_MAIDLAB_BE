package kernel.maidlab.admin.manager.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;

import kernel.maidlab.domain.manager.entity.Manager;
import kernel.maidlab.domain.manager.entity.QManager;
import kernel.maidlab.domain.manager.entity.QManagerRegion;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class AdminManagerRepositoryCustomImpl implements AdminManagerRepositoryCustom {

	private final JPAQueryFactory queryFactory;

	@Override
	public Page<Manager> findManagersByRegionId(Long regionId, Pageable pageable) {
		QManager manager = QManager.manager;
		QManagerRegion managerRegion = QManagerRegion.managerRegion;

		List<Manager> managers = queryFactory
			.selectFrom(manager)
			.join(managerRegion).on(manager.id.eq(managerRegion.manager.id))
			.where(managerRegion.regionId.id.eq(regionId)
				.and(manager.isDeleted.eq(false)))
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();

		Long total = queryFactory
			.select(manager.count())
			.from(manager)
			.join(managerRegion).on(manager.id.eq(managerRegion.manager.id))
			.where(managerRegion.regionId.id.eq(regionId)
				.and(manager.isDeleted.eq(false)))
			.fetchOne();

		return new PageImpl<>(managers, pageable, total != null ? total : 0L);
	}
}