package kernel.maidlab.domain.reservation.entity;

import jakarta.persistence.*;
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

}
