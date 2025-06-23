package kernel.maidlab.api.consumer.repository;

import kernel.maidlab.common.entity.manager.Manager;
import kernel.maidlab.common.entity.consumer.ManagerPreference;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ManagerPreferenceRepository extends JpaRepository<ManagerPreference, Long> {

    long deleteByConsumerIdAndManagerId(Long consumerId, Long managerId);

}
