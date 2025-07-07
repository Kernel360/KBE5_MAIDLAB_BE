package kernel.maidlab.common.entity.point;

import jakarta.persistence.*;
import kernel.maidlab.common.entity.base.TimeBase;
import kernel.maidlab.common.entity.consumer.Consumer;
import kernel.maidlab.common.entity.event.Event;
import kernel.maidlab.common.enums.PointType;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
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

    public void addPointFromPaymentAmount(int paymentAmount) {
        int earnedPoint = (int) Math.floor(paymentAmount * 0.01); // 결제금액의 1%
        if (earnedPoint > 0) {
            this.amount += earnedPoint;
        }
    }

    public void addPoint(Integer point){
        this.amount += point;
    }

    public void deductPoint(Integer point){

        if (amount - point < 0){
            amount = 0;
        }
        amount -= point;
    }
}

