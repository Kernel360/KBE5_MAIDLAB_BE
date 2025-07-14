package kernel.maidlab.api.reservation.dto.request;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CheckInOutRequestDto {
	LocalDateTime checkTime;
}
