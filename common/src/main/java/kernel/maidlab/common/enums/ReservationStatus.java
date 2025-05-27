package kernel.maidlab.common.enums;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "예약 상태 유형", example = "PENDING")
public enum ReservationStatus {
	PENDING, CONFIRM, CANCELED, COMPLETED
}
