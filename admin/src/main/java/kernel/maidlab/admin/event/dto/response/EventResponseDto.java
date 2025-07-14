package kernel.maidlab.admin.event.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class EventResponseDto {

	private Long eventId;
	private String title;
	private String mainImageUrl;
	private String imageUrl;
	private String content;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;

}
