package kernel.maidlab.reservation.enums;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "추가 서비스 유형", example = "COOKING")
public enum AdditionalService {
	COOKING, IRONING, PREPARE, CLEANING, TOOLS
}
