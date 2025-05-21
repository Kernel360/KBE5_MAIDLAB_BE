package kernel.maidlab.consumer.controller;




import kernel.maidlab.common.dto.ConsumerProfileDto;
import kernel.maidlab.consumer.entity.ConsumerProfile;
import kernel.maidlab.consumer.service.ConsumerProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping()
@RequiredArgsConstructor
public class ConsumerController {

    private final ConsumerProfileService consumerProfileService;

    // 프로필 생성
    @PostMapping("/api/consumers/me/profile")
    public ResponseEntity createProfile(@RequestBody ConsumerProfileDto ConsumerProfileDto){
        consumerProfileService.createConsumerProfile(ConsumerProfileDto);
        return new ResponseEntity(HttpStatus.CREATED);
    }


    // 프로필 조회
    @GetMapping("/api/consumers/me/profile/{id}")
    public ResponseEntity<ConsumerProfile> getMyProfile(@PathVariable Long id) {
        ConsumerProfile consumerProfile = consumerProfileService.findProfile(id);
        return new ResponseEntity(consumerProfile, HttpStatus.OK);
    }

    // 프로필 수정
    @PatchMapping("/api/consumers/me/profile/{id}")
    public ResponseEntity modifiedMyProfile(
                                            @PathVariable Long id,
                                            String name,
                                            String address) {
        consumerProfileService.modifyProfile(id, name, address);
        return new ResponseEntity(HttpStatus.OK);
    }

    // 프로필 삭제
    @DeleteMapping("/api/consumers/me/profile/{id}")
    public ResponseEntity deleteMyProfile(@PathVariable Long id) {
        consumerProfileService.deleteProfile(id);
        return new ResponseEntity(HttpStatus.OK);
    }
}
