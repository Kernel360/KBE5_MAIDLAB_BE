package kernel.maidlab.api.matching.dto.response;

import kernel.maidlab.common.enums.Status;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class MatchingResponseDto {
	private Long reservationId;
	private Long managerId;
	private Status matchingStatus = Status.PENDING;
}
