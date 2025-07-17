package kernel.maidlab.domain.reservation.dto.response;

import kernel.maidlab.common.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReservationDetailResponseDto {
    private Status status;
    private String serviceType;
    private String serviceDetailType;

    private String address;
    private String addressDetail;

    private String managerUuid;
    private String managerName;
    private String managerProfileImageUrl;
    private Float managerAverageRate;
    private List<String> managerRegion;
    private String managerPhoneNumber;

    private String housingType;
    private Integer roomSize;
    private String housingInformation;

    private LocalDateTime reservationDate;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    private String serviceAdd;
    private String pet;

    private String specialRequest;

    private BigDecimal totalPrice;
    private BigDecimal finalPaymentPrice;
}
