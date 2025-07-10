package kernel.maidlab.common.entity.point;

import jakarta.persistence.*;
import kernel.maidlab.common.entity.base.TimeBase;
import kernel.maidlab.common.entity.consumer.Consumer;
import kernel.maidlab.common.entity.event.Event;
import kernel.maidlab.common.entity.reservation.Reservation;
import kernel.maidlab.common.enums.PointType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "point")
public class Point extends TimeBase {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "consumer_id", nullable = false)
    private Consumer consumer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id")
    private Event event;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reservation_id")
    private Reservation reservation;

    @Column(nullable = false)
    private Integer amount;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private PointType pointType;

    @Column(nullable = false)
    private String description;

    public static Integer calculateEarnedPoint(int paymentAmount){

        int earnedPoint =  (int) Math.floor(paymentAmount * 0.01); // 결제금액의 1%
        return Math.max(earnedPoint, 0);
    }

    public static Point createPaymentPoint(Consumer consumer, Reservation reservation, Integer amount, boolean isEarned) {
        Integer pointAmount;
        String description;
        
        if (isEarned) {
            pointAmount = amount;
            description = "결제 적립 포인트";
        } else {
            pointAmount = -Math.abs(amount);
            description = "결제 사용 포인트";
        }
        
        return new Point(
                consumer,
                null,
                reservation,
                pointAmount,
                PointType.PAYMENT,
                description
        );
    }

    public static Point createEarnPointOnPayment(Consumer consumer, Reservation reservation, BigDecimal totalPrice) {
        int payAmount = totalPrice.intValue();
        Integer earnedPoint = calculateEarnedPoint(payAmount);
        return createPaymentPoint(consumer, reservation, earnedPoint, true);
    }
    
    public static Point createUsagePoint(Consumer consumer, Reservation reservation, Integer usageAmountPoint) {
        return createPaymentPoint(consumer, reservation, usageAmountPoint, false);
    }
}

