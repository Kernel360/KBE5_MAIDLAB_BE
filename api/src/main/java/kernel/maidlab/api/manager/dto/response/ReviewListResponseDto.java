package kernel.maidlab.api.manager.dto.response;

import java.util.List;

import kernel.maidlab.api.manager.dto.object.ReviewListItem;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ReviewListResponseDto {

	private List<ReviewListItem> reviews;

}
