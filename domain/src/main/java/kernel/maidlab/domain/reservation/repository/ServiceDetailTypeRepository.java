package kernel.maidlab.domain.reservation.repository;

import kernel.maidlab.domain.reservation.entity.ServiceDetailType;
import kernel.maidlab.common.enums.ServiceType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ServiceDetailTypeRepository extends JpaRepository<ServiceDetailType, Long> {
	List<ServiceDetailType> findByServiceType(ServiceType type);
}
