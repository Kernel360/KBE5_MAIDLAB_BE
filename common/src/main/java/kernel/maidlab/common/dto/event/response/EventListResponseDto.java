package kernel.maidlab.common.dto.event.response;

import java.util.List;

import kernel.maidlab.common.dto.event.object.EventListItem;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class EventListResponseDto {

	private List<EventListItem> eventList;

}
