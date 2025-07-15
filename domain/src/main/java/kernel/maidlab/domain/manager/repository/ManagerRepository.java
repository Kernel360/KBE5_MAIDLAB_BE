package kernel.maidlab.domain.manager.repository;

import kernel.maidlab.domain.manager.entity.Manager;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ManagerRepository extends JpaRepository<Manager, Long>, ManagerRepositoryCustom {
	Optional<Manager> findByUuid(String uuid);

	Optional<Manager> findByPhoneNumber(String phoneNumber);

}
