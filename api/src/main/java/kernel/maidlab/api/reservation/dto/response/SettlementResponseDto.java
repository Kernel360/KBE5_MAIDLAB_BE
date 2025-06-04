package kernel.maidlab.api.reservation.dto.response;

import java.math.BigDecimal;

import kernel.maidlab.common.enums.ServiceType;
import kernel.maidlab.common.enums.Status;
import lombok.Getter;

@Getter
public class SettlementResponseDto {
	private final Long settlementId;
	private final ServiceType serviceType;
	private final String serviceDetailType;
	private final Status status;
	private final BigDecimal platformFee;
	private final BigDecimal amount;

	public SettlementResponseDto(Long id, ServiceType serviceType, String serviceDetailType, Status status,
		BigDecimal platformFee, BigDecimal amount) {
		this.settlementId = id;
		this.serviceType = serviceType;
		this.serviceDetailType = serviceDetailType;
		this.status = status;
		this.platformFee = platformFee;
		this.amount = amount;
	}
}
