package kernel.maidlab.domain.consumer.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import kernel.maidlab.common.entity.base.Base;
import kernel.maidlab.domain.manager.entity.Manager;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ManagerPreference extends Base {

	@ManyToOne
	@JoinColumn(name = "consumer_id", nullable = false)
	private Consumer consumer;

	@ManyToOne
	@JoinColumn(name = "manager_id", nullable = false)
	private Manager manager;

	private boolean preference;

}
