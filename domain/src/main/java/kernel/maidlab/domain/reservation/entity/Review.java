package kernel.maidlab.domain.reservation.entity;

import jakarta.persistence.*;
import kernel.maidlab.common.entity.TimeBase;
import kernel.maidlab.domain.reservation.dto.request.ReviewRegisterRequestDto;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "review")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Review extends TimeBase {
    @Column(name = "reservation_id", nullable = false)
    private Long reservationId;
    @Column(name = "manager_id", nullable = false)
    private Long managerId;
    @Column(name = "consumer_id", nullable = false)
    private Long consumerId;
    @Column(name = "rating", nullable = false)
    private float rating;
    @Column(name = "comment", nullable = false)
    private String comment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "service_detail_type_id", nullable = false)
    private ServiceDetailType serviceDetailType;

    @Column(name = "review_date", nullable = false)
    private LocalDateTime reviewDate;

    @Column(name = "is_consumer_to_manager", nullable = false)
    private Boolean isConsumerToManager;

    @PrePersist
    public void prePersist() {
        if (reviewDate == null) {
            this.reviewDate = LocalDateTime.now();
        }
    }

    private Review(Long reservationId, Long managerId, Long consumerId, float rating, String comment,
                   ServiceDetailType serviceType, Boolean isConsumerToManager) {
        this.reservationId = reservationId;
        this.managerId = managerId;
        this.consumerId = consumerId;
        this.rating = rating;
        this.comment = comment;
        this.serviceDetailType = serviceType;
        this.isConsumerToManager = isConsumerToManager;
    }

    public static Review of(ReviewRegisterRequestDto dto, Reservation reservation, Boolean isConsumerToManager) {
        return new Review(reservation.getId(), reservation.getManagerId(), reservation.getConsumerId(),
                dto.getRating(), dto.getComment(), reservation.getServiceDetailType(), isConsumerToManager);
    }
}
