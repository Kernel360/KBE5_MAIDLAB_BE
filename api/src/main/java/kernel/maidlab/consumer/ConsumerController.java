package kernel.maidlab.consumer;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import kernel.maidlab.common.dto.ResponseDto;
import kernel.maidlab.common.enums.UserType;
import kernel.maidlab.core.aop.annotation.auth.AuthRequired;
import kernel.maidlab.domain.consumer.dto.ConsumerMyPageDto;
import kernel.maidlab.domain.consumer.dto.request.ConsumerProfileRequestDto;
import kernel.maidlab.domain.consumer.dto.request.ConsumerProfileUpdateRequestDto;
import kernel.maidlab.domain.consumer.dto.response.ConsumerProfileResponseDto;
import kernel.maidlab.domain.consumer.service.ConsumerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/consumers")
@RequiredArgsConstructor
public class ConsumerController {

	private final ConsumerService consumerService;

	@GetMapping("/mypage")
	@AuthRequired(roles = {UserType.CONSUMER})
	public ResponseEntity<ResponseDto<ConsumerMyPageDto>> getMyPage() {
		ConsumerMyPageDto myPageDto = consumerService.getConsumerMyPage();
		return ResponseDto.success(myPageDto);
	}

	@GetMapping("/profile")
	@AuthRequired(roles = {UserType.CONSUMER})
	public ResponseEntity<ResponseDto<ConsumerProfileResponseDto>> getProfile() {
		ConsumerProfileResponseDto responseDto = consumerService.getConsumerProfile();
		return ResponseDto.success(responseDto);
	}

	@PostMapping("/profile")
	@AuthRequired(roles = {UserType.CONSUMER})
	public ResponseEntity<ResponseDto<Void>> createProfile(
		@Validated @RequestBody ConsumerProfileRequestDto consumerProfileRequestDto) {
		consumerService.createConsumerProfile(consumerProfileRequestDto);
		return ResponseDto.success();
	}

	@PatchMapping("/profile")
	@AuthRequired(roles = {UserType.CONSUMER})
	public ResponseEntity<ResponseDto<Void>> updateProfile(
		@Validated @RequestBody ConsumerProfileUpdateRequestDto consumerProfileUpdateRequestDto) {
		consumerService.updateConsumerProfile(consumerProfileUpdateRequestDto);
		return ResponseDto.success();
	}

	@GetMapping("/likes")
	@AuthRequired(roles = {UserType.CONSUMER})
	public ResponseEntity<ResponseDto<Object>> getLikes() {
		var likedManagers = consumerService.getLikedManagerList();
		return ResponseDto.success(likedManagers);
	}

	@GetMapping("/blacklists")
	@AuthRequired(roles = {UserType.CONSUMER})
	public ResponseEntity<ResponseDto<Object>> getBlackListedManagerList() {
		var blacklistedManagers = consumerService.getBlackListedManagerList();
		return ResponseDto.success(blacklistedManagers);
	}

	@PostMapping("/preference/{managerUuid}")
	@AuthRequired(roles = {UserType.CONSUMER})
	public ResponseEntity<ResponseDto<Void>> setManagerPreference(
		@PathVariable String managerUuid,
		@RequestParam boolean preference) {
		consumerService.saveLikedOrBlackListedManager(managerUuid, preference);
		return ResponseDto.success();
	}

	@DeleteMapping("/preference/{managerUuid}")
	@AuthRequired(roles = {UserType.CONSUMER})
	public ResponseEntity<ResponseDto<String>> deleteManagerPreference(
		@PathVariable String managerUuid) {
		consumerService.deleteLikedAOrBlackListManager(managerUuid);
		return ResponseDto.success("삭제 완료");
	}
}
