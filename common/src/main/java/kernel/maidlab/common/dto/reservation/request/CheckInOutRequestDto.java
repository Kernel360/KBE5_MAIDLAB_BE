package kernel.maidlab.common.dto.reservation.request;

import java.time.LocalDateTime;

import lombok.Getter;

@Getter
public class CheckInOutRequestDto {
	LocalDateTime checkTime;
}
