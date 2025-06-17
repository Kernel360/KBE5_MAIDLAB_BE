package kernel.maidlab.common.dto.event.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class EventRequestDto {

	@NotBlank
	private String title;

	@NotBlank
	private String mainImageUrl;

	private String imageUrl;
	private String content;

}
