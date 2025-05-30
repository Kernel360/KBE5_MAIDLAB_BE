package kernel.maidlab.api.manager.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import kernel.maidlab.api.manager.entity.Region;

public interface RegionRepository extends JpaRepository<Region, Long> {
}
