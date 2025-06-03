package kernel.maidlab.common.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "service_type")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ServiceType extends Base {

	@Column(name = "service_type", nullable = false)
	private String serviceType;

	@Column(name = "detail_service_type")
	private String detailServiceType;

	@Column(name = "price")
	private Double price;

}
