package kernel.maidlab.admin.event.dto.object;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class EventListItem {

	@NotNull
	private Long eventId;

	@NotBlank
	private String title;

	@NotBlank
	private String mainImageUrl;

	@NotNull
	private LocalDateTime createdAt;

}
