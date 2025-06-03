package kernel.maidlab.api.manager.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import kernel.maidlab.api.auth.entity.Manager;
import kernel.maidlab.common.entity.Base;
import kernel.maidlab.common.entity.ServiceType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "manager_service")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ManagerService extends Base {

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "manager_id", nullable = false)
	private Manager manager;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "service_id", nullable = false)
	private ServiceType serviceType;

	private ManagerService(Manager manager, ServiceType serviceType) {
		this.manager = manager;
		this.serviceType = serviceType;
	}

	public static ManagerService managerService(Manager manager, ServiceType serviceType) {
		return new ManagerService(manager, serviceType);
	}

}
