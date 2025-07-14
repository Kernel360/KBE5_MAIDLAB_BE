package kernel.maidlab.api.consumer.controller;

import kernel.maidlab.common.dto.consumer.ConsumerMyPageDto;
import kernel.maidlab.common.dto.consumer.request.ConsumerProfileRequestDto;
import kernel.maidlab.common.dto.consumer.request.ConsumerProfileUpdateRequestDto;
import kernel.maidlab.common.dto.consumer.response.ConsumerProfileResponseDto;
import kernel.maidlab.api.consumer.service.ConsumerService;
import kernel.maidlab.common.dto.ResponseDto;
import kernel.maidlab.core.aop.annotation.auth.AuthRequired;
import kernel.maidlab.common.enums.UserType;
import kernel.maidlab.core.aop.aspect.auth.AuthenticationAspect;
import kernel.maidlab.core.security.CustomUserDetails;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
