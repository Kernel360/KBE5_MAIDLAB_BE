package kernel.maidlab.api.event.dto.response;

import java.util.List;

import kernel.maidlab.api.event.dto.object.EventListItem;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class EventListResponseDto {

	private List<EventListItem> eventList;

}
