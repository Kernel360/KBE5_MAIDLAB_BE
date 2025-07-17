package kernel.maidlab.domain.reservation.dto.response;

import kernel.maidlab.common.enums.Status;
import kernel.maidlab.domain.manager.enums.ServiceType;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class SettlementResponseDto {
    private final Long reservationId;
    private final Long settlementId;
    private final ServiceType serviceType;
    private final String serviceDetailType;
    private final Status status;
    private final BigDecimal platformFee;
    private final BigDecimal amount;

    public SettlementResponseDto(Long settlementId, Long reservationId, ServiceType serviceType,
                                 String serviceDetailType, Status status,
                                 BigDecimal platformFee, BigDecimal amount) {
        this.settlementId = settlementId;
        this.reservationId = reservationId;
        this.serviceType = serviceType;
        this.serviceDetailType = serviceDetailType;
        this.status = status;
        this.platformFee = platformFee;
        this.amount = amount;
    }
}
