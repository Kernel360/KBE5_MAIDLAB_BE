package kernel.maidlab.domain.manager.dto.response;

import kernel.maidlab.domain.manager.dto.object.ReviewListItem;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class ReviewListResponseDto {

	private List<ReviewListItem> reviews;

}
