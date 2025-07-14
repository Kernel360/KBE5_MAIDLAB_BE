package kernel.maidlab.api.consumer.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import kernel.maidlab.common.entity.consumer.ManagerPreference;

public interface ManagerPreferenceRepository extends JpaRepository<ManagerPreference, Long> {

	long deleteByConsumerIdAndManagerId(Long consumerId, Long managerId);

}
