package kernel.maidlab.domain.aws.service;

import java.util.List;

import kernel.maidlab.domain.manager.entity.Manager;

public interface RedisService {
	public void saveManagerList(String key, List<Manager> managers, long ttlMinutes);
}
