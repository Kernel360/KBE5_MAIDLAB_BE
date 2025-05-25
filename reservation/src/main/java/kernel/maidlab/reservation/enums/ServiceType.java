package kernel.maidlab.reservation.enums;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "서비스 유형", example = "HOUSEKEEPING")
public enum ServiceType {
	HOUSEKEEPING, CLEANING, CARE // 가사, 청소, 돌봄
}
