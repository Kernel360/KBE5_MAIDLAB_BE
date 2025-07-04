package kernel.maidlab.common.entity.point;

import jakarta.persistence.*;
import kernel.maidlab.common.entity.base.TimeBase;
import kernel.maidlab.common.entity.consumer.Consumer;
import kernel.maidlab.common.entity.event.Event;
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

    @Column(nullable = false)
    private Integer amount;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private PointType pointType;

    /**
     * 결제 테이블이 없으므로 String으로 관리
     */
    @Column(name = "payment_reference", length = 50)
    private String paymentReference;

    @Column(nullable = false)
    private String description;

    public static Integer calculateEarnedPoint(int paymentAmount){
        int earnedPoint =  (int) Math.floor(paymentAmount * 0.01); // 결제금액의 1%
        return Math.max(earnedPoint, 0);
    }

    public static Point createPointForPayment(Consumer consumer, BigDecimal totalPrice, String paymentReference){
        int payAmount = totalPrice.intValue();
        Integer amount = calculateEarnedPoint(payAmount);

        return new Point(
                consumer,
                null,  // 이벤트 없음
                amount,
                PointType.PAYMENT,
                paymentReference,
                "결제 적립 포인트"
        );
    }
}

