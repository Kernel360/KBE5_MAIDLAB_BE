package kernel.maidlab.api.consumer.repository;

import kernel.maidlab.api.auth.entity.Manager;
import kernel.maidlab.api.consumer.entity.ManagerPreference;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ManagerPreferenceRepository extends JpaRepository<ManagerPreference, Long> {

    @Query("SELECT DISTINCT m FROM ManagerPreference mp " +
            "JOIN mp.manager m " +  // fetch 제거
            "LEFT JOIN FETCH m.regions " +
            "WHERE mp.consumer.id = :consumerId AND mp.preference = true")
    List<Manager> findLikedManagersWithRegions(@Param("consumerId") Long consumerId);

    @Query("SELECT DISTINCT m FROM ManagerPreference mp " +
            "JOIN mp.manager m " +
            "WHERE mp.consumer.id = :consumerId AND mp.preference = false ")
    List<Manager> findBlackListedManagers(@Param("consumerId") Long consumerId);

    long deleteByConsumerIdAndManagerIdAndPreferenceIsTrue(Long consumerId, Long managerId);


}
