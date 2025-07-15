package kernel.maidlab.domain.consumer.repository;

import java.util.List;

import kernel.maidlab.domain.manager.entity.Manager;

public interface ManagerPreferenceRepositoryCustom {

	List<Manager> findManagersByPreference(Long consumerId, boolean preference);

}
