package kernel.maidlab.api.manager.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import kernel.maidlab.common.entity.manager.ManagerSchedule;

@Repository
public interface ManagerScheduleRepository extends JpaRepository<ManagerSchedule, Long> {
	List<ManagerSchedule> findByManagerId(Long managerId);

	@Modifying
	@Query("DELETE FROM ManagerSchedule ms WHERE ms.manager.id = :managerId")
	void deleteByManagerId(@Param("managerId") Long managerId);
}
