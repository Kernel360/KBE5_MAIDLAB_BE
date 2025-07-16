package kernel.maidlab.domain.util;

import java.math.BigDecimal;

public class RoomSizeRuleUtil {

	private static final int[] ROOM_SIZE_BY_IDX = {8, 9, 11, 16, 21, 26, 31, 35};
	private static final BigDecimal[] ESTIMATED_PRICES = {BigDecimal.valueOf(52500), BigDecimal.valueOf(54600),
		BigDecimal.valueOf(63000), BigDecimal.valueOf(64000), BigDecimal.valueOf(74250), BigDecimal.valueOf(75600),
		BigDecimal.valueOf(76500), BigDecimal.valueOf(78000)};

	public static int resolveRoomSize(Integer idx) {
		if (idx == null || idx < 0 || idx >= ROOM_SIZE_BY_IDX.length) {
			throw new IllegalArgumentException("유효하지 않은 평수 인덱스입니다.");
		}
		return ROOM_SIZE_BY_IDX[idx];
	}

	public static BigDecimal resolveBasePrice(Integer idx) {
		if (idx == null || idx < 0 || idx >= ESTIMATED_PRICES.length) {
			throw new IllegalArgumentException("유효하지 않은 금액 인덱스입니다.");
		}
		return ESTIMATED_PRICES[idx];
	}
}
