package kernel.maidlab.api.consumer.repository;

import kernel.maidlab.api.manager.entity.Manager;

import java.util.List;


public interface ManagerPreferenceRepositoryCustom {

	List<Manager> findManagersByPreference(Long consumerId, boolean preference);

}
