package kernel.maidlab.api.matching.service;

import java.util.List;

import kernel.maidlab.common.entity.manager.Manager;

public interface RedisService {
	public void saveManagerList(String key, List<Manager> managers, long ttlMinutes);
}
