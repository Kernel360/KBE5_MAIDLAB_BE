package kernel.maidlab.common.entity.matching;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import kernel.maidlab.common.dto.matching.response.MatchingResponseDto;
import kernel.maidlab.common.entity.Base.Base;
import kernel.maidlab.common.enums.Status;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "matching")
@Getter
@NoArgsConstructor
public class Matching extends Base {

	@Column
	private Long reservationId;

	@Setter
	@Column
	private Long managerId;

	@Setter
	@Column
	@Enumerated(EnumType.STRING)
	private Status matchingStatus;

	private Matching (
		Long reservationId,
		Long managerId,
		Status matchingStatus
	) {
		this.reservationId = reservationId;
		this.managerId = managerId;
		this.matchingStatus = matchingStatus;
	}

	public static Matching of(MatchingResponseDto dto){
		return new Matching(
			dto.getReservationId(),
			dto.getManagerId(),
			dto.getMatchingStatus()
		);
	}

}
