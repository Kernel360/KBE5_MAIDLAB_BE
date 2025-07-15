package kernel.maidlab.domain.reservation.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import kernel.maidlab.common.enums.ServiceType;
import kernel.maidlab.domain.reservation.entity.ServiceDetailType;

public interface ServiceDetailTypeRepository extends JpaRepository<ServiceDetailType, Long> {
	List<ServiceDetailType> findByServiceType(ServiceType type);
}
