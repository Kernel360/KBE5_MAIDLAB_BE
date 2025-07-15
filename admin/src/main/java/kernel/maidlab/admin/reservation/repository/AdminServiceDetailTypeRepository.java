package kernel.maidlab.admin.reservation.repository;

import kernel.maidlab.api.reservation.entity.ServiceDetailType;
import kernel.maidlab.common.enums.ServiceType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AdminServiceDetailTypeRepository extends JpaRepository<ServiceDetailType, Long> {
	List<ServiceDetailType> findByServiceType(ServiceType type);
}
