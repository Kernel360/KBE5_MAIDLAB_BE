package kernel.maidlab.api.manager.repository;

import kernel.maidlab.api.auth.entity.Manager;
import kernel.maidlab.api.manager.entity.ManagerRegion;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ManagerRegionRepository extends JpaRepository<ManagerRegion, String> {
	List<ManagerRegion> findByManagerId(Manager manager);

	@Query(value = "SELECT r.region FROM manager_region mr JOIN region r ON mr.region_id = r.id WHERE mr.managerId.id = :managerId", nativeQuery = true)
	List<String> findRegionNamesByManagerId(@Param("managerId") Long managerId);

	@Modifying
	@Query("DELETE FROM ManagerSchedule ms WHERE ms.manager.id = :managerId")
	void deleteByManagerId(@Param("managerId") Long managerId);
}
