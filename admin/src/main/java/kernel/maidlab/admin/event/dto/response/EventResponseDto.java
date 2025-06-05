package kernel.maidlab.admin.event.dto.response;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class EventResponseDto {

	private Long eventId;
	private String title;
	private String imageUrl;
	private String content;
	private LocalDateTime createdAt;

}
