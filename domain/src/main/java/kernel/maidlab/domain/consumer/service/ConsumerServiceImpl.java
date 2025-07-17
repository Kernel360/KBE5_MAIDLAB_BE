package kernel.maidlab.domain.consumer.service;

import kernel.maidlab.common.enums.ResponseType;
import kernel.maidlab.common.enums.UserType;
import kernel.maidlab.core.aop.annotation.exception.ExceptionHandler;
import kernel.maidlab.core.aop.enums.LogLevel;
import kernel.maidlab.core.security.AuthenticationHelper;
import kernel.maidlab.domain.consumer.dto.ConsumerMyPageDto;
import kernel.maidlab.domain.consumer.dto.request.ConsumerProfileRequestDto;
import kernel.maidlab.domain.consumer.dto.request.ConsumerProfileUpdateRequestDto;
import kernel.maidlab.domain.consumer.dto.response.BlackListedManagerResponseDto;
import kernel.maidlab.domain.consumer.dto.response.ConsumerProfileResponseDto;
import kernel.maidlab.domain.consumer.dto.response.LikedManagerResponseDto;
import kernel.maidlab.domain.consumer.entity.Consumer;
import kernel.maidlab.domain.consumer.entity.ManagerPreference;
import kernel.maidlab.domain.consumer.repository.ConsumerRepository;
import kernel.maidlab.domain.consumer.repository.ManagerPreferenceRepository;
import kernel.maidlab.domain.consumer.repository.ManagerPreferenceRepositoryCustom;
import kernel.maidlab.domain.manager.entity.Manager;
import kernel.maidlab.domain.manager.repository.ManagerRepository;
import kernel.maidlab.domain.util.UserValidator;
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
    @ExceptionHandler(
            value = {IllegalArgumentException.class},
            responseType = ResponseType.THIS_RESOURCE_DOES_NOT_EXIST,
            message = "매니저를 찾을 수 없습니다",
            logLevel = LogLevel.WARN,
            enableNotification = true
    )
    public void saveLikedOrBlackListedManager(String managerUuid, boolean preference) {
        Consumer consumer = getConsumer();
        Manager manager = managerRepository.findByUuid(managerUuid)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 매니저 입니다."));
        ManagerPreference managerPreference = new ManagerPreference(consumer, manager, preference);
        managerPreferenceRepository.save(managerPreference);
        log.info("매니저 선호도 저장 - Consumer ID: {}, Manager UUID: {}, 선호도: {}", consumer.getId(), managerUuid, preference);
    }

    @Override
    @ExceptionHandler(
            value = {IllegalArgumentException.class},
            responseType = ResponseType.THIS_RESOURCE_DOES_NOT_EXIST,
            message = "매니저를 찾을 수 없습니다",
            logLevel = LogLevel.WARN,
            enableNotification = true
    )
    public long deleteLikedAOrBlackListManager(String managerUuid) {
        Consumer consumer = getConsumer();
        Manager manager = managerRepository.findByUuid(managerUuid)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 매니저입니다."));
        long deletedCount = managerPreferenceRepository.deleteByConsumerIdAndManagerId(consumer.getId(), manager.getId());
        log.info("매니저 선호도 삭제 - Consumer ID: {}, Manager UUID: {}, 삭제된 레코드 수: {}", consumer.getId(), managerUuid, deletedCount);
        return deletedCount;
    }

    @Override
    public Consumer getConsumer() {
        String userId = AuthenticationHelper.getCurrentUserKey();
        return userValidator.findByUuid(userId, UserType.CONSUMER);
    }

    @Transactional(readOnly = true)
    @Override
    @ExceptionHandler(
            value = {IllegalArgumentException.class},
            responseType = ResponseType.THIS_RESOURCE_DOES_NOT_EXIST,
            message = "소비자를 찾을 수 없습니다",
            logLevel = LogLevel.WARN
    )
    public Consumer findById(Long consumerId) {
        return consumerRepository.findById(consumerId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 소비자입니다. ID: " + consumerId));
    }
}
