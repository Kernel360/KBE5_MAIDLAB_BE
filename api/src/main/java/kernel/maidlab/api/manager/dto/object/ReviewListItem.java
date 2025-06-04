package kernel.maidlab.api.manager.dto.object;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

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