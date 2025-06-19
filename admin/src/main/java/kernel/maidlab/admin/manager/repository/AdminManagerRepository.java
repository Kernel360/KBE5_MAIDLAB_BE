package kernel.maidlab.admin.manager.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Range;
import org.springframework.data.jpa.repository.JpaRepository;

import kernel.maidlab.common.entity.manager.Manager;
import kernel.maidlab.common.enums.Status;

public interface AdminManagerRepository extends JpaRepository<Manager, Long> {

	Page<Manager> findAllByIsVerified(Status status, Pageable pageable);
}
