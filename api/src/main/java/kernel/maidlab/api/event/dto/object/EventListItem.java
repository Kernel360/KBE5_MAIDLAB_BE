package kernel.maidlab.api.event.dto.object;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class EventListItem {

	@NotBlank
	private Long eventId;

	@NotBlank
	private String title;

	@NotBlank
	private String mainImageUrl;

	@NotBlank
	private LocalDateTime createdAt;

}
