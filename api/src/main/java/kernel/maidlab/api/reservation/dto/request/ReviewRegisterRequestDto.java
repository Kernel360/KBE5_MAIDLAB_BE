package kernel.maidlab.api.reservation.dto.request;


import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Getter
public class ReviewRegisterRequestDto {
	private float rating;
	private String comment;
	private boolean likes;
}
