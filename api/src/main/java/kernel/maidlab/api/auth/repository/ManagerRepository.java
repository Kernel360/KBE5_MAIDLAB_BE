package kernel.maidlab.api.auth.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import kernel.maidlab.api.auth.entity.Manager;
import kernel.maidlab.api.manager.repository.ManagerRepositoryCustom;

public interface ManagerRepository extends JpaRepository<Manager, Long>, ManagerRepositoryCustom {
	Optional<Manager> findByUuid(String uuid);
	Optional<Manager> findByPhoneNumber(String phoneNumber);
}
