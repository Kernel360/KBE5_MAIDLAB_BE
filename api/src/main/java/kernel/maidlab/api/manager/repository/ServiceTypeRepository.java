package kernel.maidlab.api.manager.repository;

import kernel.maidlab.common.entity.ServiceType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ServiceTypeRepository extends JpaRepository<ServiceType, Long> {
	Optional<ServiceType> findByServiceType(String serviceType);
}