package kernel.maidlab.api.consumer.service;

import kernel.maidlab.core.security.AuthenticationHelper;
import kernel.maidlab.api.consumer.repository.ConsumerRepository;
import kernel.maidlab.api.consumer.repository.ManagerPreferenceRepository;
import kernel.maidlab.api.consumer.repository.ManagerPreferenceRepositoryCustom;
import kernel.maidlab.api.manager.repository.ManagerRepository;
import kernel.maidlab.common.dto.consumer.ConsumerMyPageDto;
import kernel.maidlab.common.dto.consumer.request.ConsumerProfileRequestDto;
import kernel.maidlab.common.dto.consumer.request.ConsumerProfileUpdateRequestDto;
import kernel.maidlab.common.dto.consumer.response.BlackListedManagerResponseDto;
import kernel.maidlab.common.dto.consumer.response.ConsumerProfileResponseDto;
import kernel.maidlab.common.dto.consumer.response.LikedManagerResponseDto;
import kernel.maidlab.common.entity.consumer.Consumer;
import kernel.maidlab.common.entity.consumer.ManagerPreference;
import kernel.maidlab.common.entity.manager.Manager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ConsumerService {

	private final ConsumerRepository consumerRepository;
	private final ManagerPreferenceRepository managerPreferenceRepository;
	private final ManagerPreferenceRepositoryCustom managerPreferenceRepositoryCustom;
	private final ManagerRepository managerRepository;

	@Transactional(readOnly = true)
	public ConsumerMyPageDto getConsumerMyPage(String userId){
		Consumer findedConsumer = getConsumer(userId);
		return ConsumerMyPageDto.getInstance(findedConsumer);
	}


	@Transactional(readOnly = true)
	public ConsumerProfileResponseDto getConsumerProfile(String userId) {
		Consumer consumer = getConsumer(userId);
		return ConsumerProfileResponseDto.getInstance(consumer);
	}

	public void createConsumerProfile(
			ConsumerProfileRequestDto consumerProfileRequestDto,
			String userId)
	{
		Consumer consumer = getConsumer(userId);

		String profileImage = consumerProfileRequestDto.getProfileImage();
		String address = consumerProfileRequestDto.getAddress();
		String detailAddress = consumerProfileRequestDto.getDetailAddress();

		consumer.createProfile(profileImage, address, detailAddress);
		consumerRepository.save(consumer);
	}

	public void updateConsumerProfile(
			ConsumerProfileUpdateRequestDto consumerProfileUpdateRequestDto,
			String userId){
		Consumer consumer = getConsumer(userId);
		consumer.updateProfile(consumerProfileUpdateRequestDto);
		consumerRepository.save(consumer);
	}


	// 찜한 매니저 조회
	@Transactional(readOnly = true)
	public List<LikedManagerResponseDto> getLikedManagerList() {
		String userId = AuthenticationHelper.getCurrentUserId();
		Consumer consumer = getConsumer(userId);

		List<Manager> likedManagerList = managerPreferenceRepositoryCustom
				.findManagersByPreference(consumer.getId(), true);

		return LikedManagerResponseDto.getLikedManagerResponseDtoList(likedManagerList);
	}

	// 블랙 리스트 매니저 조회
	@Transactional(readOnly = true)
	public List<BlackListedManagerResponseDto> getBlackListedManagerList() {
		String userId = AuthenticationHelper.getCurrentUserId();
		Consumer consumer = getConsumer(userId);

		List<Manager> BlacklistedManagerList = managerPreferenceRepositoryCustom
				.findManagersByPreference(consumer.getId(), false);

		return BlackListedManagerResponseDto.getManagerResponseDtoList(BlacklistedManagerList);
	}

	// 찜/블랙리스트 매니저 등록
	public void saveLikedOrBlackListedManager(
			String managerUuid,
			boolean preference) {
		String userId = AuthenticationHelper.getCurrentUserId();
		Consumer consumer = getConsumer(userId);

		Manager manager = managerRepository.findByUuid(managerUuid).
			orElseThrow(() -> new IllegalArgumentException("존재하지 않는 매니저 입니다."));

		ManagerPreference managerPreference = new ManagerPreference(consumer, manager, preference);
		managerPreferenceRepository.save(managerPreference);
	}

	public long deleteLikedAOrBlackListManager(
			String managerUuid) {
		String userId = AuthenticationHelper.getCurrentUserId();
		Consumer consumer = getConsumer(userId);

		Manager manager = managerRepository.findByUuid(managerUuid)
			.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 매니저입니다."));

		return managerPreferenceRepository.
				deleteByConsumerIdAndManagerId(
					consumer.getId(),
					manager.getId());
	}

	public Consumer getConsumer(String userId){
		return consumerRepository.findByUuid(userId).orElse(null);
	}

}
