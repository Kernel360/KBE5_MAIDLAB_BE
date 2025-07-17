package kernel.maidlab.domain.consumer.repository;

import kernel.maidlab.domain.consumer.entity.ManagerPreference;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ManagerPreferenceRepository extends JpaRepository<ManagerPreference, Long> {

    long deleteByConsumerIdAndManagerId(Long consumerId, Long managerId);

}
