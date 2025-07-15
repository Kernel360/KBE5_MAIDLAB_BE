package kernel.maidlab.domain.aws.service;

import kernel.maidlab.domain.manager.entity.Manager;

import java.util.List;


public interface RedisService {
	public void saveManagerList(String key, List<Manager> managers, long ttlMinutes);
}
