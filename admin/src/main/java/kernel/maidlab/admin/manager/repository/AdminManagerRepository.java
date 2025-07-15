package kernel.maidlab.admin.manager.repository;

import kernel.maidlab.domain.manager.entity.Manager;
import kernel.maidlab.common.enums.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminManagerRepository extends JpaRepository<Manager, Long> {

	Page<Manager> findAllByIsVerified(Status status, Pageable pageable);

	Long countByIsDeletedFalseAndIsVerified(Status status);
}
