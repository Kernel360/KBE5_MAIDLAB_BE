package kernel.maidlab.domain.reservation.dto.request;

import java.time.LocalDateTime;

import lombok.Getter;

@Getter
public class CheckInOutRequestDto {
	LocalDateTime checkTime;
}
