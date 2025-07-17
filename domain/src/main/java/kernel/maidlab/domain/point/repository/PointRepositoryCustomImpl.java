package kernel.maidlab.domain.point.repository;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kernel.maidlab.domain.point.dto.response.PointRecordResponseDto;
import kernel.maidlab.domain.point.entity.Point;
import kernel.maidlab.domain.point.entity.QPoint;
import kernel.maidlab.domain.point.enums.PointType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class PointRepositoryCustomImpl implements PointRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Long getTotalPointsByConsumerId(Long consumerId) {

        QPoint point = QPoint.point;

        Long total = queryFactory
                .select(Expressions.numberTemplate(Long.class, "sum({0})", point.amount))
                .from(point)
                .where(point.consumer.id.eq(consumerId))
                .fetchOne();

        return total != null ? total : 0L;
    }

    @Override
    public Page<PointRecordResponseDto> findPointRecords(
            Long consumerId,
            Integer monthOffset,
            String pointType,
            Pageable pageable) {
        QPoint point = QPoint.point;

        JPQLQuery<Point> query = queryFactory
                .selectFrom(point)
                .where(
                        point.consumer.id.eq(consumerId),
                        eqPointType(pointType, point),
                        inMonthOffset(monthOffset, point)
                );

        //  정렬 처리
        List<OrderSpecifier<?>> orderSpecifiers = new ArrayList<>();
        for (Sort.Order order : pageable.getSort()) {
            Order direction = order.isAscending() ? Order.ASC : Order.DESC;
            PathBuilder<Point> entityPath = new PathBuilder<>(Point.class, point.getMetadata());
            orderSpecifiers.add(new OrderSpecifier(direction, entityPath.get(order.getProperty())));
        }
        query.orderBy(orderSpecifiers.toArray(new OrderSpecifier[0]));

        long total = query.fetchCount();

        List<PointRecordResponseDto> content = query
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch()
                .stream()
                .map(PointRecordResponseDto::from)
                .toList();

        return new PageImpl<>(content, pageable, total);
    }

    private BooleanExpression eqPointType(String pointType, QPoint point) {
        if (pointType == null || pointType.equalsIgnoreCase("ALL")) {
            return null;
        }
        return point.pointType.eq(PointType.valueOf(pointType));
    }

    private BooleanExpression inMonthOffset(Integer monthOffset, QPoint point) {
        LocalDate now = LocalDate.now().plusMonths(monthOffset != null ? monthOffset : 0);
        LocalDateTime startOfMonth = now.withDayOfMonth(1).atStartOfDay();
        LocalDateTime endOfMonth = now.withDayOfMonth(now.lengthOfMonth()).atTime(23, 59, 59);
        return point.createdAt.between(startOfMonth, endOfMonth);
    }
}
