package kernel.maidlab.api.consumer.service;

import jakarta.transaction.Transactional;
import kernel.maidlab.api.auth.entity.Consumer;
import kernel.maidlab.api.auth.entity.Manager;
import kernel.maidlab.api.consumer.dto.response.ConsumerProfileResponseDto;
import kernel.maidlab.api.consumer.repository.ConsumerRepository;
import kernel.maidlab.api.manager.repository.ManagerRepository;
import kernel.maidlab.api.consumer.dto.request.ConsumerProfileRequestDto;
import kernel.maidlab.api.consumer.dto.response.BlackListedManagerResponseDto;
import kernel.maidlab.api.consumer.dto.response.ConsumerListResponseDto;
import kernel.maidlab.api.consumer.dto.response.LikedManagerResponseDto;
import kernel.maidlab.api.consumer.entity.ManagerPreference;
import kernel.maidlab.api.consumer.repository.ManagerPreferenceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Pageable;
import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ConsumerService {

    private final ConsumerRepository consumerRepository;
    private final ManagerPreferenceRepository managerPreferenceRepository;
    private final ManagerRepository managerRepository;

    public Consumer getConsumerByUuid(String uuid) {
        return consumerRepository.findByUuid(uuid)
            .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
    }

    public ConsumerProfileResponseDto getConsumerProfile(String uuid) {
        Consumer consumer = getConsumerByUuid(uuid);

        return ConsumerProfileResponseDto.builder()
            .profileImage(consumer.getProfileImage())
            .phoneNumber(consumer.getPhoneNumber())
            .name(consumer.getName())
            .birth(consumer.getBirth())
            .gender(consumer.getGender())
            .address(consumer.getAddress())
            .detailAddress(consumer.getDetailAddress())
            .build();
    }

    public void updateConsumerProfile(String uuid, ConsumerProfileRequestDto req) {
        Consumer consumer = getConsumerByUuid(uuid);

        String profileImage = req.getProfileImage();
        String address = req.getAddress();
        String detailAddress = req.getDetailAddress();

        consumer.updateProfile(profileImage, address, detailAddress);
        consumerRepository.save(consumer);
    }

    // 찜한 매니저 조회
    public List<LikedManagerResponseDto> getLikeManagerList(Consumer consumer) {

        List<Manager> likedManagerList = managerPreferenceRepository.findLikedManagersWithRegions(consumer.getId());
        return likedManagerList.stream()
                .map(m -> new LikedManagerResponseDto(
                        m.getUuid(),
                        m.getName(),
                        m.getProfileImage(),
                        m.getAverageRate(),
                        m.getIntroduceText(),
                        m.getRegions()   // 여기서 바로 enum 리스트
                ))
                .toList();
    }

    // 블랙 리스트 매니저 조회
    public List<BlackListedManagerResponseDto> getBlackListedManagerList(Consumer consumer) {

        List<Manager> BlacklistedManagerList = managerPreferenceRepository.findBlackListedManagers(consumer.getId());
        return BlacklistedManagerList.stream()
                .map(m -> new BlackListedManagerResponseDto(
                        m.getUuid(),
                        m.getName(),
                        m.getProfileImage(),
                        m.getAverageRate(),
                        m.getIntroduceText()
                ))
                .toList();
    }

    // 찜/블랙리스트 매니저 등록
    public void saveLikedOrBlackListedManager(String consumerUuid, String managerUuid, boolean preference){

        Consumer consumer = getConsumerByUuid(consumerUuid);
        Manager manager = managerRepository.findByUuid(managerUuid).
                orElseThrow(() -> new IllegalArgumentException("존재하지 않는 매니저 입니다."));

        ManagerPreference managerPreference = new ManagerPreference(consumer, manager, preference);
        managerPreferenceRepository.save(managerPreference);

    }

    public long deleteLikedManager(String consumerUuid, String managerUuid) {
        Consumer consumer = getConsumerByUuid(consumerUuid);
        Manager manager = managerRepository.findByUuid(managerUuid)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 매니저입니다."));

        long deletedCount = managerPreferenceRepository.deleteByConsumerIdAndManagerIdAndPreferenceIsTrue(
            consumer.getId(), manager.getId());


        return deletedCount;
    }

    // 관리자용 전체조회로직
    public Page<ConsumerListResponseDto> getConsumerBypage(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return consumerRepository.findAll(pageable)
            .map(consumer -> new ConsumerListResponseDto(
                consumer.getId(),
                consumer.getPhoneNumber(),
                consumer.getName(),
                consumer.getUuid()
            ));
    }

    public ConsumerProfileResponseDto getConsumerProfileById(long id) {
        Consumer consumer = consumerRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        return ConsumerProfileResponseDto.builder()
            .profileImage(consumer.getProfileImage())
            .phoneNumber(consumer.getPhoneNumber())
            .name(consumer.getName())
            .birth(consumer.getBirth())
            .gender(consumer.getGender())
            .address(consumer.getAddress())
            .detailAddress(consumer.getDetailAddress())
            .build();
    }
}
