package kernel.maidlab.api.consumer.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kernel.maidlab.api.consumer.repository.ConsumerRepository;
import kernel.maidlab.api.consumer.repository.ManagerPreferenceRepository;
import kernel.maidlab.api.consumer.repository.ManagerPreferenceRepositoryCustom;
import kernel.maidlab.api.manager.repository.ManagerRepository;
import kernel.maidlab.api.util.UserValidator;
import kernel.maidlab.common.dto.consumer.ConsumerMyPageDto;
import kernel.maidlab.common.dto.consumer.request.ConsumerProfileRequestDto;
import kernel.maidlab.common.dto.consumer.request.ConsumerProfileUpdateRequestDto;
import kernel.maidlab.common.dto.consumer.response.BlackListedManagerResponseDto;
import kernel.maidlab.common.dto.consumer.response.ConsumerProfileResponseDto;
import kernel.maidlab.common.dto.consumer.response.LikedManagerResponseDto;
import kernel.maidlab.common.entity.consumer.Consumer;
import kernel.maidlab.common.entity.consumer.ManagerPreference;
import kernel.maidlab.common.entity.manager.Manager;
import kernel.maidlab.common.enums.UserType;
import kernel.maidlab.core.security.AuthenticationHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ConsumerServiceImpl implements ConsumerService {

	private final ConsumerRepository consumerRepository;
	private final ManagerPreferenceRepository managerPreferenceRepository;
	private final ManagerPreferenceRepositoryCustom managerPreferenceRepositoryCustom;
	private final ManagerRepository managerRepository;
	private final UserValidator userValidator;

	@Transactional(readOnly = true)
	@Override
	public ConsumerMyPageDto getConsumerMyPage() {
		Consumer findedConsumer = getConsumer();
		return ConsumerMyPageDto.from(findedConsumer);
	}

	@Transactional(readOnly = true)
	@Override
	public ConsumerProfileResponseDto getConsumerProfile() {
		Consumer consumer = getConsumer();
		return ConsumerProfileResponseDto.from(consumer);
	}

	@Override
	public void createConsumerProfile(ConsumerProfileRequestDto consumerProfileRequestDto) {
		Consumer consumer = getConsumer();
		consumer.createProfile(consumerProfileRequestDto);
		consumerRepository.save(consumer);
	}

	@Override
	public void updateConsumerProfile(ConsumerProfileUpdateRequestDto consumerProfileUpdateRequestDto) {
		Consumer consumer = getConsumer();
		consumer.updateProfile(consumerProfileUpdateRequestDto);
		consumerRepository.save(consumer);
	}

	@Transactional(readOnly = true)
	@Override
	public List<LikedManagerResponseDto> getLikedManagerList() {
		Consumer consumer = getConsumer();
		List<Manager> likedManagerList = managerPreferenceRepositoryCustom
			.findManagersByPreference(consumer.getId(), true);
		return LikedManagerResponseDto.from(likedManagerList);
	}

	@Transactional(readOnly = true)
	@Override
	public List<BlackListedManagerResponseDto> getBlackListedManagerList() {
		Consumer consumer = getConsumer();
		List<Manager> blacklistedManagerList = managerPreferenceRepositoryCustom
			.findManagersByPreference(consumer.getId(), false);
		return BlackListedManagerResponseDto.from(blacklistedManagerList);
	}

	@Override
	public void saveLikedOrBlackListedManager(String managerUuid, boolean preference) {
		Consumer consumer = getConsumer();
		Manager manager = managerRepository.findByUuid(managerUuid)
			.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 매니저 입니다."));
		ManagerPreference managerPreference = new ManagerPreference(consumer, manager, preference);
		managerPreferenceRepository.save(managerPreference);
	}

	@Override
	public long deleteLikedAOrBlackListManager(String managerUuid) {
		Consumer consumer = getConsumer();
		Manager manager = managerRepository.findByUuid(managerUuid)
			.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 매니저입니다."));
		return managerPreferenceRepository.deleteByConsumerIdAndManagerId(consumer.getId(), manager.getId());
	}

	@Override
	public Consumer getConsumer() {
		String userId = AuthenticationHelper.getCurrentUserId();
		return userValidator.findByUuid(userId, UserType.CONSUMER);
	}

	@Transactional(readOnly = true)
	@Override
	public Consumer findById(Long consumerId) {
		return consumerRepository.findById(consumerId)
			.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 소비자입니다. ID: " + consumerId));
	}
}
