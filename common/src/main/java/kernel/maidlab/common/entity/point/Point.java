package kernel.maidlab.common.entity.point;

import jakarta.persistence.*;
import kernel.maidlab.common.entity.base.TimeBase;
import kernel.maidlab.common.entity.consumer.Consumer;
import kernel.maidlab.common.entity.event.Event;
import kernel.maidlab.common.entity.reservation.Reservation;
import kernel.maidlab.common.enums.PointType;
import lombok.AllArgsConstructor;
import lombok.Builder;
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

    public static Point createEarnPointOnPayment(Consumer consumer, Reservation  reservation, BigDecimal totalPrice){

        int payAmount = totalPrice.intValue();
        Integer earnedPoint = calculateEarnedPoint(payAmount);

        return new Point(
                consumer,
                null,  // 이벤트 없음
                reservation,
                earnedPoint,
                PointType.PAYMENT,
                "결제 적립 포인트"
        );
    }

    @Builder
    public static Point createUsagePoint(Consumer consumer, Reservation reservation, Integer usageAmountPoint){

        return new Point(
                consumer,
                null,
                reservation,
                (-Math.abs(usageAmountPoint)),
                PointType.PAYMENT,
                "결제 사용 포인트"

        );
    }
}

