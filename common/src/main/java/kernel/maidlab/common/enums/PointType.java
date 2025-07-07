package kernel.maidlab.common.enums;

import lombok.Getter;

@Getter
public enum PointType {

    PAYMENT("결제"),
    EVENT("이벤트");

    private final String name;

    PointType(String name) { this.name= name;}
}
