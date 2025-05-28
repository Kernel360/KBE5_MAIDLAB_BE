package kernel.maidlab.api.manager.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "managersite")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class ManagerSite {
	@Id
	@Column(name = "id", nullable = false)
	private Long Id;

	@Column(name = "managerid", nullable = false)
	private Long ManagerId;

	@Column(name = "siteid", nullable = false)
	private Long SiteId;
}
