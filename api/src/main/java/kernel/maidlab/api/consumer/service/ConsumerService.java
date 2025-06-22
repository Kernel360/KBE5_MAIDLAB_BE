package kernel.maidlab.api.consumer.service;

import jakarta.servlet.http.HttpServletRequest;
import kernel.maidlab.api.auth.jwt.JwtFilter;
import kernel.maidlab.api.consumer.repository.ConsumerRepository;
import kernel.maidlab.api.consumer.repository.ManagerPreferenceRepository;
import kernel.maidlab.api.consumer.repository.ManagerPreferenceRepositoryCustom;
import kernel.maidlab.api.manager.repository.ManagerRepository;
import kernel.maidlab.common.dto.consumer.ConsumerMyPageDto;
import kernel.maidlab.common.dto.consumer.request.ConsumerProfileRequestDto;
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
	public ConsumerMyPageDto getConsumerMyPage(HttpServletRequest req){

		Consumer findedConsumer = getConsumer(req);
		return ConsumerMyPageDto.getInstance(findedConsumer);
	}


	@Transactional(readOnly = true)
	public ConsumerProfileResponseDto getConsumerProfile(HttpServletRequest req) {

		Consumer consumer = getConsumer(req);
		return ConsumerProfileResponseDto.getInstance(consumer);
	}

	public void updateConsumerProfile(
			ConsumerProfileRequestDto consumerProfileRequestDto,
			HttpServletRequest req)
	{
		Consumer consumer = getConsumer(req);

		String profileImage = consumerProfileRequestDto.getProfileImage();
		String address = consumerProfileRequestDto.getAddress();
		String detailAddress = consumerProfileRequestDto.getDetailAddress();

		consumer.updateProfile(profileImage, address, detailAddress);
		consumerRepository.save(consumer);
	}

	// 찜한 매니저 조회
	@Transactional(readOnly = true)
	public List<LikedManagerResponseDto> getLikedManagerList(HttpServletRequest req) {

		Consumer consumer = getConsumer(req);

		List<Manager> likedManagerList = managerPreferenceRepositoryCustom
				.findManagersByPreference(consumer.getId(), true);

		return LikedManagerResponseDto.getLikedManagerResponseDtoList(likedManagerList);
	}

	// 블랙 리스트 매니저 조회
	@Transactional(readOnly = true)
	public List<BlackListedManagerResponseDto> getBlackListedManagerList(HttpServletRequest req) {

		Consumer consumer = getConsumer(req);

		List<Manager> BlacklistedManagerList = managerPreferenceRepositoryCustom
				.findManagersByPreference(consumer.getId(), false);

		return BlackListedManagerResponseDto.getManagerResponseDtoList(BlacklistedManagerList);
	}

	// 찜/블랙리스트 매니저 등록
	public void saveLikedOrBlackListedManager(
			HttpServletRequest req,
			String managerUuid,
			boolean preference) {

		Consumer consumer = getConsumer(req);

		Manager manager = managerRepository.findByUuid(managerUuid).
			orElseThrow(() -> new IllegalArgumentException("존재하지 않는 매니저 입니다."));

		ManagerPreference managerPreference = new ManagerPreference(consumer, manager, preference);
		managerPreferenceRepository.save(managerPreference);
	}

	public long deleteLikedAOrBlackListManager(
			String managerUuid,
			HttpServletRequest req) {

		Consumer consumer = getConsumer(req);

		Manager manager = managerRepository.findByUuid(managerUuid)
			.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 매니저입니다."));

		return managerPreferenceRepository.
				deleteByConsumerIdAndManagerId(
					consumer.getId(),
					manager.getId());
	}

	public Consumer getConsumer(HttpServletRequest req){
		return (Consumer)req.getAttribute(JwtFilter.CURRENT_USER_KEY);
	}

}
