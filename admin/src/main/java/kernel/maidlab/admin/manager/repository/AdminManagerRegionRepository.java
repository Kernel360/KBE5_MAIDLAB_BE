package kernel.maidlab.admin.manager.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import kernel.maidlab.common.entity.manager.ManagerRegion;

@Repository
public interface AdminManagerRegionRepository extends JpaRepository<ManagerRegion, String> {
	List<ManagerRegion> findByManagerId(Long managerId);

	@Query("SELECT r.regionName FROM ManagerRegion mr JOIN mr.regionId r WHERE mr.manager.id = :managerId")
	List<String> findRegionNamesByManagerId(@Param("managerId") Long managerId);

	@Modifying
	@Query("DELETE FROM ManagerRegion mr WHERE mr.manager.id = :managerId")
	void deleteByManagerId(@Param("managerId") Long managerId);
}
