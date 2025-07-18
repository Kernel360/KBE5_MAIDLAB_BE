package kernel.maidlab.admin.manager.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import kernel.maidlab.domain.manager.entity.Manager;

public interface AdminManagerRepositoryCustom {
	Page<Manager> findManagersByRegionId(Long regionId, Pageable pageable);
}