package kernel.maidlab.domain.manager.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import kernel.maidlab.domain.manager.entity.Manager;

public interface ManagerRepository extends JpaRepository<Manager, Long>, ManagerRepositoryCustom {
	Optional<Manager> findByUuid(String uuid);

	Optional<Manager> findByPhoneNumber(String phoneNumber);

}
