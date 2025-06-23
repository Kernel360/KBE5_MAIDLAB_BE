package kernel.maidlab.api.consumer.repository;


import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kernel.maidlab.common.entity.consumer.QManagerPreference;
import kernel.maidlab.common.entity.manager.Manager;
import kernel.maidlab.common.entity.manager.QManager;
import kernel.maidlab.common.entity.manager.QRegion;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ManagerPreferenceRepositoryCustomImpl implements ManagerPreferenceRepositoryCustom{

    private final JPAQueryFactory jpaQueryFactory;

    public List<Manager> findManagersByPreference(Long consumerId, boolean preference) {
        QManager m = QManager.manager;
        QManagerPreference mp = QManagerPreference.managerPreference;

        JPAQuery<Manager> query = jpaQueryFactory
                .selectDistinct(m)
                .from(mp)
                .join(mp.manager, m);

        // preference가 true일 때만 regions fetch join
        if (preference) {
            query.leftJoin(m.regions).fetchJoin();
        }

        query.where(
                mp.consumer.id.eq(consumerId),
                mp.preference.eq(preference)
        );

        return query.fetch();
    }
}
