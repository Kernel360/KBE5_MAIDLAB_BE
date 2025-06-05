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

	@Query("SELECT r.regionName FROM ManagerRegion mr JOIN mr.regionId r WHERE mr.manager.id = :managerId")
	List<String> findRegionNamesByManagerId(@Param("managerId") Long managerId);

	@Modifying
	@Query("DELETE FROM ManagerRegion mr WHERE mr.manager.id = :managerId")
	void deleteByManagerId(@Param("managerId") Long managerId);
}
