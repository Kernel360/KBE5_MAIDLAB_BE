package kernel.maidlab.api.consumer.service;

import jakarta.servlet.http.HttpServletRequest;
import kernel.maidlab.common.dto.consumer.ConsumerMyPageDto;
import kernel.maidlab.common.dto.consumer.request.ConsumerProfileRequestDto;
import kernel.maidlab.common.dto.consumer.request.ConsumerProfileUpdateRequestDto;
import kernel.maidlab.common.dto.consumer.response.BlackListedManagerResponseDto;
import kernel.maidlab.common.dto.consumer.response.ConsumerProfileResponseDto;
import kernel.maidlab.common.dto.consumer.response.LikedManagerResponseDto;
import kernel.maidlab.common.entity.consumer.Consumer;

import java.util.List;

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
