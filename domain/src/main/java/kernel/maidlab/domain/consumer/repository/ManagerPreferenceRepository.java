package kernel.maidlab.domain.consumer.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import kernel.maidlab.domain.consumer.entity.ManagerPreference;

public interface ManagerPreferenceRepository extends JpaRepository<ManagerPreference, Long> {

	long deleteByConsumerIdAndManagerId(Long consumerId, Long managerId);

}
