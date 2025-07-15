package kernel.maidlab.domain.consumer.service;

import java.util.List;

import kernel.maidlab.domain.consumer.dto.ConsumerMyPageDto;
import kernel.maidlab.domain.consumer.dto.request.ConsumerProfileRequestDto;
import kernel.maidlab.domain.consumer.dto.request.ConsumerProfileUpdateRequestDto;
import kernel.maidlab.domain.consumer.dto.response.BlackListedManagerResponseDto;
import kernel.maidlab.domain.consumer.dto.response.ConsumerProfileResponseDto;
import kernel.maidlab.domain.consumer.dto.response.LikedManagerResponseDto;
import kernel.maidlab.domain.consumer.entity.Consumer;

public interface ConsumerService {

	ConsumerMyPageDto getConsumerMyPage();

	ConsumerProfileResponseDto getConsumerProfile();

	void createConsumerProfile(ConsumerProfileRequestDto consumerProfileRequestDto);

	void updateConsumerProfile(ConsumerProfileUpdateRequestDto consumerProfileUpdateRequestDto);

	List<LikedManagerResponseDto> getLikedManagerList();

	List<BlackListedManagerResponseDto> getBlackListedManagerList();

	void saveLikedOrBlackListedManager(String managerUuid, boolean preference);

	long deleteLikedAOrBlackListManager(String managerUuid);

	Consumer getConsumer();

	Consumer findById(Long consumerId);
}
