package kernel.maidlab.domain.reservation.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@AllArgsConstructor
public class WeeklySettlementResponseDto {
    private BigDecimal totalAmount;
    private List<SettlementResponseDto> settlements;
}
