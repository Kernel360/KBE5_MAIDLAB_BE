package kernel.maidlab.domain.manager.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import kernel.maidlab.common.enums.ServiceType;
import kernel.maidlab.domain.manager.entity.ManagerServiceType;

@Repository
public interface ManagerServiceTypeRepository extends JpaRepository<ManagerServiceType, Long> {
	List<ManagerServiceType> findByManagerId(Long managerId);

	@Query("SELECT ms.serviceType FROM ManagerServiceType ms WHERE ms.manager.id = :managerId")
	List<ServiceType> findServiceTypesByManagerId(@Param("managerId") Long managerId);

	@Modifying
	@Query("DELETE FROM ManagerServiceType ms WHERE ms.manager.id = :managerId")
	void deleteByManagerId(@Param("managerId") Long managerId);
}
