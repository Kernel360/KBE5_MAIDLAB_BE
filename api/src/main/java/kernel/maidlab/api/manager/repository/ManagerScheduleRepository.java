package kernel.maidlab.api.manager.repository;

import kernel.maidlab.api.manager.entity.ManagerSchedule;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ManagerScheduleRepository extends JpaRepository<ManagerSchedule, Long> {
	List<ManagerSchedule> findByManagerId(Long managerId);

	@Modifying
	@Query("DELETE FROM ManagerSchedule ms WHERE ms.manager.id = :managerId")
	void deleteByManagerId(@Param("managerId") Long managerId);
}