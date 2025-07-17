package kernel.maidlab.domain.consumer.repository;

import kernel.maidlab.domain.manager.entity.Manager;

import java.util.List;

public interface ManagerPreferenceRepositoryCustom {

    List<Manager> findManagersByPreference(Long consumerId, boolean preference);

}
