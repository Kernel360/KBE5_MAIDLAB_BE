package kernel.maidlab.domain.manager.enums;

import java.math.BigDecimal;

public enum ServiceOptionType {
    WINDOW_CLEANING("창문 유리/커튼 및 블라인드 청소", 60, BigDecimal.valueOf(16900), false), FAN_CLEANING("선풍기 청소", 10,
            BigDecimal.valueOf(4600), true), SHOES_CLEANING("운동화 세탁", 20, BigDecimal.valueOf(5900), true), IRONING("다림질",
            50, BigDecimal.valueOf(12600), false);

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
