package kernel.maidlab.api.auth.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import kernel.maidlab.api.auth.entity.Manager;

public interface ManagerRepository extends JpaRepository<Manager, Long> {
	Optional<Manager> findByUuid(String uuid);
}
