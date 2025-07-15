package kernel.maidlab.domain.reservation.dto.request;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class ReviewRegisterRequestDto {
	private Long reservationId;
	private float rating;
	private String comment;
	private Boolean likes;
	private List<String> keywords;
}
