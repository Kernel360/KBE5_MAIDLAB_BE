package kernel.maidlab.common.dto.matching.request;

import lombok.Data;

@Data
public class MatchingRequestDto {
	private String address;
	private String startTime;
	private String endTime;
	private Boolean managerChoose;
}
