package kernel.maidlab.common.dto.manager.object;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RegionListItem {

	@NotBlank
	private String region;

}
