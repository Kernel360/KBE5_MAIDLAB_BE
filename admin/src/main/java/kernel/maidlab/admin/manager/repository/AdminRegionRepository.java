package kernel.maidlab.admin.manager.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import kernel.maidlab.domain.manager.entity.Region;

@Repository
public interface AdminRegionRepository extends JpaRepository<Region, Long> {
	Optional<Region> findByRegionName(String region);
}
