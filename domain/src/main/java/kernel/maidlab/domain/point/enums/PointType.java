package kernel.maidlab.domain.point.enums;

import lombok.Getter;

@Getter
public enum PointType {

    PAYMENT("결제"),
    EVENT("이벤트"),
    CHARGE("충전");

    private final String type;

    PointType(String type) {
        this.type = type;
    }
}
