package kernel.maidlab.api.reservation.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import kernel.maidlab.common.enums.ServiceType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "service_detail_type")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ServiceDetailType {
	@Id
	private Long id;

	@Column(nullable = false)
	private String serviceDetailType;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private ServiceType serviceType;

	@Column(nullable = false)
	private Long servicePrice;
}
