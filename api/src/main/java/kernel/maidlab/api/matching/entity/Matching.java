package kernel.maidlab.api.matching.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import kernel.maidlab.api.matching.dto.MatchingDto;
import kernel.maidlab.api.reservation.entity.Reservation;
import kernel.maidlab.common.entity.Base;
import kernel.maidlab.common.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "matching")
@Getter
@NoArgsConstructor
@Builder
public class Matching extends Base {

	@Column
	private Long reservationId;

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

	public static Matching of(MatchingDto dto){
		return new Matching(
			dto.getReservationId(),
			dto.getManagerId(),
			dto.getMatchingStatus()
		);
	}

}
