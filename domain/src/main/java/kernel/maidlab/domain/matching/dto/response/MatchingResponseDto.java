package kernel.maidlab.domain.matching.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import kernel.maidlab.common.enums.Status;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MatchingResponseDto {
    private Long reservationId;
    private Long managerId;
    private Status matchingStatus = Status.PENDING;
    private Integer matchingCount = 1;

    public MatchingResponseDto() {
    }

    public MatchingResponseDto(Long managerId, Long reservationId, Status matchingStatus) {
        this.managerId = managerId;
        this.reservationId = reservationId;
        this.matchingStatus = matchingStatus;
        this.matchingCount = 1;
    }

    public MatchingResponseDto(Long reservationId, Long managerId, Status matchingStatus, Integer matchingCount,
                               LocalDateTime updatedAt) {
        this.reservationId = reservationId;
        this.managerId = managerId;
        this.matchingStatus = matchingStatus;
        this.matchingCount = matchingCount;
        this.updatedAt = updatedAt;
    }

    @JsonFormat(pattern = "yyyy.MM.dd HH:mm")
    private LocalDateTime updatedAt;

}
