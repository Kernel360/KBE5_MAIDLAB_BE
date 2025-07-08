package kernel.maidlab.common.dto.matching.response;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import kernel.maidlab.common.enums.Status;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class MatchingResponseDto {
	private Long reservationId;
	private Long managerId;
	private Status matchingStatus = Status.PENDING;
	private Integer matchingCount = 1;
	
	@JsonFormat(pattern = "yyyy.MM.dd HH:mm")
	private LocalDateTime updatedAt;
}
