package kernel.maidlab.api.matching.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import kernel.maidlab.api.reservation.entity.Reservation;
import kernel.maidlab.common.entity.Base;
import kernel.maidlab.common.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "matching")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Matching extends Base {

	@Column
	private Long reservationId;

	@Column
	private Long managerId;

	@Column
	@Enumerated(EnumType.STRING)
	private Status matchingStatus;

}
