package kernel.maidlab.api.reservation.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class AdminWeeklySettlementResponseDto {
	private BigDecimal totalAmount;
	private Page<AdminSettlementResponseDto> settlements;
}
