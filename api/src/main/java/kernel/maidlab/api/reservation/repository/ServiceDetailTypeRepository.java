package kernel.maidlab.api.reservation.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import kernel.maidlab.common.enums.ServiceType;
import kernel.maidlab.common.entity.reservation.ServiceDetailType;

public interface ServiceDetailTypeRepository extends JpaRepository<ServiceDetailType, Long> {
	List<ServiceDetailType> findByServiceType(ServiceType type);
}
