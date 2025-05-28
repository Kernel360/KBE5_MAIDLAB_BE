package kernel.maidlab.api.manager.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import kernel.maidlab.api.manager.entity.Manager;

public interface ManagerRepository extends JpaRepository<Manager, Long>, ManagerRepositoryCustom {

}
