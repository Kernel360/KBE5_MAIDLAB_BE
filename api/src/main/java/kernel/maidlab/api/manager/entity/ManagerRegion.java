package kernel.maidlab.api.manager.entity;

import jakarta.persistence.*;
import kernel.maidlab.common.entity.base.Base;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "manager_region")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ManagerRegion extends Base {

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "manager_id", nullable = false)
	private Manager manager;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "region_id", nullable = false)
	private Region regionId;

	private ManagerRegion(Manager manager, Region regionId) {
		this.manager = manager;
		this.regionId = regionId;
	}

	public static ManagerRegion managerRegion(Manager manager, Region regionId) {
		return new ManagerRegion(manager, regionId);
	}

}
