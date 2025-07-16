package kernel.maidlab.domain.reservation.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import kernel.maidlab.domain.manager.enums.ServiceType;
import kernel.maidlab.common.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;

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
