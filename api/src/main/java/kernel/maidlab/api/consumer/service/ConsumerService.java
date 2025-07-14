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

	ConsumerMyPageDto getConsumerMyPage(HttpServletRequest req);

	ConsumerProfileResponseDto getConsumerProfile(HttpServletRequest req);

	void createConsumerProfile(ConsumerProfileRequestDto consumerProfileRequestDto, HttpServletRequest req);

	void updateConsumerProfile(ConsumerProfileUpdateRequestDto consumerProfileUpdateRequestDto, HttpServletRequest req);

	List<LikedManagerResponseDto> getLikedManagerList(HttpServletRequest req);

	List<BlackListedManagerResponseDto> getBlackListedManagerList(HttpServletRequest req);

	void saveLikedOrBlackListedManager(HttpServletRequest req, String managerUuid, boolean preference);

	long deleteLikedAOrBlackListManager(String managerUuid, HttpServletRequest req);

	Consumer getConsumer(HttpServletRequest req);

	Consumer findById(Long consumerId);
}
