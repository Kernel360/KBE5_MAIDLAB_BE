package kernel.maidlab.domain.manager.dto.response;

import java.util.List;

import kernel.maidlab.domain.manager.dto.object.ReviewListItem;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ReviewListResponseDto {

	private List<ReviewListItem> reviews;

}
