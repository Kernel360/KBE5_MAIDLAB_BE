package kernel.maidlab.api.reservation.dto.response;

import java.math.BigDecimal;

import org.springframework.data.domain.Page;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AdminWeeklySettlementResponseDto {
	private BigDecimal totalAmount;
	private Page<AdminSettlementResponseDto> settlements;
}
