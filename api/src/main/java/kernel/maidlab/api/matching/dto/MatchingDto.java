package kernel.maidlab.api.matching.dto;

import kernel.maidlab.common.enums.Status;
import lombok.Data;

@Data
public class MatchingDto {
	private Long ReservationId;
	private Long ManagerId;
	private Status MatchingStatus = Status.PENDING;
}
