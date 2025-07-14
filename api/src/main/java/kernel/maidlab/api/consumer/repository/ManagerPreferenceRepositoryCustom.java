package kernel.maidlab.api.consumer.repository;

import java.util.List;

import kernel.maidlab.common.entity.manager.Manager;

public interface ManagerPreferenceRepositoryCustom {

	List<Manager> findManagersByPreference(Long consumerId, boolean preference);

}
