package kernel.maidlab.api.manager.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import kernel.maidlab.common.enums.Region;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "manager_region")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class ManagerRegion {
	@Id
	@Column(name = "id", nullable = false)
	private Long Id;

	@Column(name = "managerid", nullable = false)
	private Long ManagerId;

	@Column(name = "regionid", nullable = false)
	private Long regionId;
}
