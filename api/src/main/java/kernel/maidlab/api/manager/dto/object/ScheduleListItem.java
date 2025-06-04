package kernel.maidlab.api.manager.dto.object;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleListItem {

	@NotBlank
	private String day;

	@NotBlank
	@Pattern(regexp = "^([01]?[0-9]|2[0-3]):[0-5][0-9]$")
	private String startTime;

	@NotBlank
	@Pattern(regexp = "^([01]?[0-9]|2[0-3]):[0-5][0-9]$")
	private String endTime;
}
