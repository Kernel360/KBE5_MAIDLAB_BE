package kernel.maidlab.domain.manager.enums;

import java.math.BigDecimal;

public enum ServiceOptionType {
	WINDOW_CLEANING("창문 유리/커튼 및 블라인드 청소", 90, BigDecimal.valueOf(27900), false), FAN_CLEANING("선풍기 청소", 20,
		BigDecimal.valueOf(7800), true), SHOES_CLEANING("운동화 세탁", 30, BigDecimal.valueOf(8600), true), IRONING("다림질",
		60, BigDecimal.valueOf(15600), false);

	private final String label;
	private final int timeAddMinutes;
	private final BigDecimal priceAdd;
	private final boolean countable;

	ServiceOptionType(String label, int timeAddMinutes, BigDecimal priceAdd, boolean countable) {
		this.label = label;
		this.timeAddMinutes = timeAddMinutes;
		this.priceAdd = priceAdd;
		this.countable = countable;
	}

	public BigDecimal getPriceForCount(Integer count) {
		if (!countable) {
			return priceAdd;
		}
		return priceAdd.multiply(BigDecimal.valueOf(count != null ? count : 0));
	}

	public static boolean isValid(String id) {
		try {
			ServiceOptionType.valueOf(id);
			return true;
		} catch (IllegalArgumentException e) {
			return false;
		}
	}

	public static ServiceOptionType from(String id) {
		return ServiceOptionType.valueOf(id);
	}
}
