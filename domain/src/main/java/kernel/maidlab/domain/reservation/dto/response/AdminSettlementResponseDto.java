package kernel.maidlab.domain.reservation.dto.response;

import kernel.maidlab.common.enums.Status;
import kernel.maidlab.domain.manager.enums.ServiceType;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class AdminSettlementResponseDto {
    private Long SettlementId;
    private String managerName;
    private ServiceType serviceType;
    private String ServiceDetailType;
    private Status status;
    private BigDecimal amount;
    private LocalDateTime createdAt;
}
