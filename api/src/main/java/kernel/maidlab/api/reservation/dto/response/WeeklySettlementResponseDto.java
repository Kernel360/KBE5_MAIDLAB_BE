package kernel.maidlab.api.reservation.dto.response;

import java.math.BigDecimal;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class WeeklySettlementResponseDto {
	private BigDecimal totalAmount;
	private List<SettlementResponseDto> settlements;
}
