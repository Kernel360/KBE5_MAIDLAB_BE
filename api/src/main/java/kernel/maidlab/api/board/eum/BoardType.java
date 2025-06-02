package kernel.maidlab.api.board.eum;

import lombok.Getter;

@Getter
public enum BoardType {

    REFUND("환불"),
    MANAGER("매니저"),
    SERVICE("서비스"),
    ETC("기타");

    private final String value;

    BoardType(String value) {
        this.value = value;
    }
}
