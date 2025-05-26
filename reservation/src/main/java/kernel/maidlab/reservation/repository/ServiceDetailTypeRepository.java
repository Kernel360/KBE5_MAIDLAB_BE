package kernel.maidlab.reservation.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import kernel.maidlab.reservation.entity.ServiceDetailType;
import kernel.maidlab.reservation.entity.enums.ServiceType;

public interface ServiceDetailTypeRepository extends JpaRepository<ServiceDetailType, Long> {
	List<ServiceDetailType> findByServiceType(ServiceType type);
}
