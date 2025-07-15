package kernel.maidlab.domain.manager.dto.object;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ReviewListItem {

	private String reviewId;
	private BigDecimal rating;
	private String name;
	private String comment;
	private String serviceType;
	private String serviceDetailType;

}
