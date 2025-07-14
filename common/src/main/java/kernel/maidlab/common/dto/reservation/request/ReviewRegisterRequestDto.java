package kernel.maidlab.common.dto.reservation.request;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
