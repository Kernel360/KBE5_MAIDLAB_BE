package kernel.maidlab.api.consumer.repository;

import kernel.maidlab.api.consumer.entity.ManagerPreference;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ManagerPreferenceRepository extends JpaRepository<ManagerPreference, Long> {

	long deleteByConsumerIdAndManagerId(Long consumerId, Long managerId);

}
