package kernel.maidlab.api.manager.repository;

import kernel.maidlab.api.auth.entity.Manager;
import kernel.maidlab.common.enums.Status;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Range;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ManagerRepository extends JpaRepository<Manager, Long>, ManagerRepositoryCustom {
	Optional<Manager> findByUuid(String uuid);

	Optional<Manager> findByPhoneNumber(String phoneNumber);

	Page<Manager> findAllByIsVerified(Status status, Pageable pageable);
}
