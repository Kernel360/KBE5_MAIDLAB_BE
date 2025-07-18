package kernel.maidlab.admin.manager.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import kernel.maidlab.common.enums.Status;
import kernel.maidlab.domain.manager.entity.Manager;

public interface AdminManagerRepository extends JpaRepository<Manager, Long>, AdminManagerRepositoryCustom {

	Page<Manager> findAllByIsVerified(Status status, Pageable pageable);

	Long countByIsDeletedFalseAndIsVerified(Status status);
}
