package kernel.maidlab.domain.reservation.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Getter
@AllArgsConstructor
public class SettlementGraphDataDto {
	private List<DailySettlementData> dailyData;
	private List<WeeklySettlementData> weeklyData;
	private List<MonthlySettlementData> monthlyData;
	private List<ServiceTypeData> serviceTypeData;
	private List<StatusData> statusData;
	private BigDecimal totalAmount;
	private BigDecimal totalPlatformFee;
	private Long totalCount;

	@Getter
	@AllArgsConstructor
	public static class DailySettlementData {
		private LocalDate date;
		private BigDecimal amount;
		private BigDecimal platformFee;
		private Long count;
	}

	@Getter
	@AllArgsConstructor
	public static class WeeklySettlementData {
		private LocalDate weekStart;
		private LocalDate weekEnd;
		private BigDecimal amount;
		private BigDecimal platformFee;
		private Long count;
	}

	@Getter
	@AllArgsConstructor
	public static class MonthlySettlementData {
		private int year;
		private int month;
		private BigDecimal amount;
		private BigDecimal platformFee;
		private Long count;
	}

	@Getter
	@AllArgsConstructor
	public static class ServiceTypeData {
		private String serviceType;
		private BigDecimal amount;
		private Long count;
	}

	@Getter
	@AllArgsConstructor
	public static class StatusData {
		private String status;
		private BigDecimal amount;
		private Long count;
	}
}