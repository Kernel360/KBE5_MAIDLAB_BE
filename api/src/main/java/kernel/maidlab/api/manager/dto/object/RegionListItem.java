package kernel.maidlab.api.manager.dto.object;

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
