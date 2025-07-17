package kernel.maidlab.domain.point.entity;

import jakarta.persistence.*;
import kernel.maidlab.common.entity.TimeBase;
import kernel.maidlab.domain.consumer.entity.Consumer;
import kernel.maidlab.domain.point.enums.PointType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Getter
@AllArgsConstructor
@Table(name = "point")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Point extends TimeBase {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "consumer_id", nullable = false)
    private Consumer consumer;

    @Column(nullable = false)
    private Integer amount;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private PointType pointType;

    @Column(nullable = false)
    private String description;

    public static Integer calculateEarnedPoint(int paymentAmount) {

        int earnedPoint = (int) Math.floor(paymentAmount * 0.01); // 결제금액의 1%
        return Math.max(earnedPoint, 0);
    }

    public enum PointAction {
        EARN("결제 적립 포인트", true),
        USE("결제 사용 포인트", false),
        CHARGE("포인트 충전", true);

        private final String description;
        private final boolean isPositive;

        PointAction(String description, boolean isPositive) {
            this.description = description;
            this.isPositive = isPositive;
        }

        public String getDescription() {
            return description;
        }

        public boolean isPositive() {
            return isPositive;
        }
    }

    private static Point createPoint(
            Consumer consumer,
            Integer amount,
            PointType pointType,
            PointAction action) {
        Integer finalAmount = action.isPositive() ? amount : -Math.abs(amount);
        return new Point(
                consumer,
                finalAmount,
                pointType,
                action.getDescription());
    }

    public static Point createEarnPointOnPayment(
            Consumer consumer,
            BigDecimal totalPrice) {
        int payAmount = totalPrice.intValue();
        Integer earnedPoint = calculateEarnedPoint(payAmount);

        return createPoint(
                consumer,
                earnedPoint,
                PointType.PAYMENT,
                PointAction.EARN);
    }

    public static Point createUsagePoint(
            Consumer consumer,
            Integer usageAmountPoint) {
        return createPoint(
                consumer,
                usageAmountPoint,
                PointType.PAYMENT,
                PointAction.USE);
    }

    public static Point createChargePoint(
            Consumer consumer,
            Integer chargeAmount) {
        return createPoint(
                consumer,
                chargeAmount,
                PointType.CHARGE,
                PointAction.CHARGE);
    }
}

