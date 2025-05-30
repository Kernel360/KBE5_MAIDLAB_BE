package kernel.maidlab.api.manager.repository;

import kernel.maidlab.api.auth.entity.Manager;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ManagerRepository extends JpaRepository<Manager, Long>, ManagerRepositoryCustom {
	Optional<Manager> findByUuid(String uuid);

	Optional<Manager> findByPhoneNumber(String phoneNumber);
}
