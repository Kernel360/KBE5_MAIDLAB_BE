package kernel.maidlab.api.manager.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import kernel.maidlab.api.manager.entity.ManagerServiceType;

@Repository
public interface ManagerServiceTypeRepository extends JpaRepository<ManagerServiceType, Long> {
	List<ManagerServiceType> findByManagerId(Long managerId);

	@Query(value = "SELECT s.service_type FROM manager_service ms JOIN service_type s ON ms.service_id = s.id WHERE ms.manager_id = :managerId", nativeQuery = true)
	List<String> findServiceTypeNamesByManagerId(@Param("managerId") Long managerId);

	@Modifying
	@Query("DELETE FROM ManagerServiceType ms WHERE ms.manager.id = :managerId")
	void deleteByManagerId(@Param("managerId") Long managerId);
}
