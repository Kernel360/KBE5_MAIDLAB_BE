package kernel.maidlab.common.dto.manager.response;

import java.util.List;

import kernel.maidlab.common.dto.manager.object.ReviewListItem;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ReviewListResponseDto {

	private List<ReviewListItem> reviews;

}
