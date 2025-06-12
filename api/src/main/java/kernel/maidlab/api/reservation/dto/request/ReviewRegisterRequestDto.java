package kernel.maidlab.api.reservation.dto.request;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class ReviewRegisterRequestDto {
	private float rating;
	private String comment;
	private Boolean likes;
}
