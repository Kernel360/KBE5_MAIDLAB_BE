package kernel.maidlab.api.consumer.service;

import kernel.maidlab.common.dto.ConsumerProfileDto;
import kernel.maidlab.api.consumer.entity.ConsumerProfile;
import kernel.maidlab.api.consumer.repository.ConsumerProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ConsumerProfileService {

    private final ConsumerProfileRepository consumerProfileRepository;

    // 프로필 생성
    public void createConsumerProfile(ConsumerProfileDto ConsumerProfileDto){
        ConsumerProfile consumerProfile = new ConsumerProfile();
        consumerProfile.setName(ConsumerProfileDto.getName());
        consumerProfile.setAddress(ConsumerProfileDto.getAddress());
        consumerProfileRepository.save(consumerProfile);
    }

    // 프로필 조회
    public ConsumerProfile findProfile(Long id) {
        return consumerProfileRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("user doesn't exist"));
    }

    // 프로필 수정
    public void modifyProfile(Long id, String name,String address) {
        ConsumerProfile consumerProfile = consumerProfileRepository.findById(id).orElseThrow(() -> new IllegalArgumentException(""));
        consumerProfile.setName(name);
        consumerProfile.setAddress(address);
    }

    // 프로필 삭제
    public void deleteProfile(Long id) {
        consumerProfileRepository.deleteById(id);
    }

}
