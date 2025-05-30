package kernel.maidlab.api.manager.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import kernel.maidlab.api.manager.entity.ManagerRegion;

public interface ManagerRegionRepository extends JpaRepository<ManagerRegion, Long> {

	List<ManagerRegion> findByManager_Id(Long managerId);

}
