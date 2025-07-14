package kernel.maidlab.api.consumer.service;

import jakarta.servlet.http.HttpServletRequest;
import kernel.maidlab.api.auth.jwt.JwtFilter;
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
public class ConsumerServiceImpl implements ConsumerService {

    private final ConsumerRepository consumerRepository;
    private final ManagerPreferenceRepository managerPreferenceRepository;
    private final ManagerPreferenceRepositoryCustom managerPreferenceRepositoryCustom;
    private final ManagerRepository managerRepository;

    @Transactional(readOnly = true)
    @Override
    public ConsumerMyPageDto getConsumerMyPage(HttpServletRequest req) {
        Consumer findedConsumer = getConsumer(req);
        return ConsumerMyPageDto.from(findedConsumer);
    }

    @Transactional(readOnly = true)
    @Override
    public ConsumerProfileResponseDto getConsumerProfile(HttpServletRequest req) {
        Consumer consumer = getConsumer(req);
        return ConsumerProfileResponseDto.from(consumer);
    }

    @Override
    public void createConsumerProfile(ConsumerProfileRequestDto consumerProfileRequestDto, HttpServletRequest req) {
        Consumer consumer = getConsumer(req);
        consumer.createProfile(consumerProfileRequestDto);
        consumerRepository.save(consumer);
    }

    @Override
    public void updateConsumerProfile(ConsumerProfileUpdateRequestDto consumerProfileUpdateRequestDto, HttpServletRequest req) {
        Consumer consumer = getConsumer(req);
        consumer.updateProfile(consumerProfileUpdateRequestDto);
        consumerRepository.save(consumer);
    }

    @Transactional(readOnly = true)
    @Override
    public List<LikedManagerResponseDto> getLikedManagerList(HttpServletRequest req) {
        Consumer consumer = getConsumer(req);
        List<Manager> likedManagerList = managerPreferenceRepositoryCustom
                .findManagersByPreference(consumer.getId(), true);
        return LikedManagerResponseDto.from(likedManagerList);
    }

    @Transactional(readOnly = true)
    @Override
    public List<BlackListedManagerResponseDto> getBlackListedManagerList(HttpServletRequest req) {
        Consumer consumer = getConsumer(req);
        List<Manager> blacklistedManagerList = managerPreferenceRepositoryCustom
                .findManagersByPreference(consumer.getId(), false);
        return BlackListedManagerResponseDto.from(blacklistedManagerList);
    }

    @Override
    public void saveLikedOrBlackListedManager(HttpServletRequest req, String managerUuid, boolean preference) {
        Consumer consumer = getConsumer(req);
        Manager manager = managerRepository.findByUuid(managerUuid)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 매니저 입니다."));
        ManagerPreference managerPreference = new ManagerPreference(consumer, manager, preference);
        managerPreferenceRepository.save(managerPreference);
    }

    @Override
    public long deleteLikedAOrBlackListManager(String managerUuid, HttpServletRequest req) {
        Consumer consumer = getConsumer(req);
        Manager manager = managerRepository.findByUuid(managerUuid)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 매니저입니다."));
        return managerPreferenceRepository.deleteByConsumerIdAndManagerId(consumer.getId(), manager.getId());
    }

    @Override
    public Consumer getConsumer(HttpServletRequest req) {
        return (Consumer) req.getAttribute(JwtFilter.CURRENT_USER_KEY);
    }

    @Transactional(readOnly = true)
    @Override
    public Consumer findById(Long consumerId) {
        return consumerRepository.findById(consumerId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 소비자입니다. ID: " + consumerId));
    }
}
