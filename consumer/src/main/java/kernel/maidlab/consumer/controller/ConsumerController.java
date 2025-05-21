package kernel.maidlab.consumer.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import kernel.maidlab.common.dto.ConsumerProfileDto;
import kernel.maidlab.consumer.entity.ConsumerProfile;
import kernel.maidlab.consumer.service.ConsumerProfileService;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Consumer", description = "Consumer API")
@RestController
@RequestMapping("/api/consumers")
@RequiredArgsConstructor
public class ConsumerController {

	private final ConsumerProfileService consumerProfileService;

	// 프로필 생성
	@Operation(summary = "프로필 생성", description = "프로필 생성 API")
	@ApiResponse(responseCode = "200", description = "프로필 등록 완료!")
	@PostMapping("/me/profile")
	public ResponseEntity createProfile(@RequestBody ConsumerProfileDto ConsumerProfileDto) {
		consumerProfileService.createConsumerProfile(ConsumerProfileDto);
		return new ResponseEntity(HttpStatus.CREATED);
	}

	// 프로필 조회
	@Operation(summary = "프로필 조회", description = "프로필 조회 API")
	@GetMapping("/me/profile/{id}")
	public ResponseEntity<ConsumerProfile> getMyProfile(@PathVariable Long id) {
		ConsumerProfile consumerProfile = consumerProfileService.findProfile(id);
		return new ResponseEntity(consumerProfile, HttpStatus.OK);
	}

	// 프로필 수정
	@Operation(summary = "프로필 수정", description = "프로필 수정 API")
	@PatchMapping("/api/consumers/me/profile/{id}")
	public ResponseEntity modifiedMyProfile(
		@PathVariable Long id,
		String name,
		String address) {
		consumerProfileService.modifyProfile(id, name, address);
		return new ResponseEntity(HttpStatus.OK);
	}

	// 프로필 삭제
	@Operation(summary = "프로필 삭제", description = "프로필 삭제 API")
	@DeleteMapping("/api/consumers/me/profile/{id}")
	public ResponseEntity deleteMyProfile(@PathVariable Long id) {
		consumerProfileService.deleteProfile(id);
		return new ResponseEntity(HttpStatus.OK);
	}
}
