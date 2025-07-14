package kernel.maidlab.admin.event.dto.object;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

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
