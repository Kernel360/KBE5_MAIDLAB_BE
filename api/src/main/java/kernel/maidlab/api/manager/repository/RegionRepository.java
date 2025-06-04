package kernel.maidlab.api.manager.repository;

import kernel.maidlab.api.manager.entity.Region;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RegionRepository extends JpaRepository<Region, Long> {
	Optional<Region> findByRegionName(String region);
}
