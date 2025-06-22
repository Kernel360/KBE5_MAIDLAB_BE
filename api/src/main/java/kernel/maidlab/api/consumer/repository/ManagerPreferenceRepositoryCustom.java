package kernel.maidlab.api.consumer.repository;

import kernel.maidlab.common.entity.manager.Manager;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface  ManagerPreferenceRepositoryCustom {

    List<Manager> findManagersByPreference(Long consumerId, boolean preference);

}
