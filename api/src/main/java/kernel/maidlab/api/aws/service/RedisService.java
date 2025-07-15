package kernel.maidlab.api.aws.service;

import kernel.maidlab.api.manager.entity.Manager;

import java.util.List;


public interface RedisService {
	public void saveManagerList(String key, List<Manager> managers, long ttlMinutes);
}
