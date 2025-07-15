package kernel.maidlab.admin.event.dto.response;

import kernel.maidlab.admin.event.dto.object.EventListItem;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class EventListResponseDto {

	private List<EventListItem> eventList;

}
