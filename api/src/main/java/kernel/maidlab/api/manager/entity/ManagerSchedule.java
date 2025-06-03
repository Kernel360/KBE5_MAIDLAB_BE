package kernel.maidlab.api.manager.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import kernel.maidlab.api.auth.entity.Manager;
import kernel.maidlab.common.entity.Base;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "manager_schedule")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ManagerSchedule extends Base {

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "manager_id", nullable = false)
	private Manager manager;

	@Column(name = "available_day", nullable = false)
	private String availableDay;

	@Column(name = "available_start_time", nullable = false)
	private String availableStartTime;

	@Column(name = "available_end_time", nullable = false)
	private String availableEndTime;

	private ManagerSchedule(Manager manager, String availableDay, String availableStartTime, String availableEndTime) {
		this.manager = manager;
		this.availableDay = availableDay;
		this.availableStartTime = availableStartTime;
		this.availableEndTime = availableEndTime;
	}

	public static ManagerSchedule managerSchedule(Manager manager, String availableDay, String availableStartTime,
		String availableEndTime) {
		return new ManagerSchedule(manager, availableDay, availableStartTime, availableEndTime);
	}
}
