package kernel.maidlab.domain.reservation.dto.request;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CheckInOutRequestDto {
    LocalDateTime checkTime;
}
