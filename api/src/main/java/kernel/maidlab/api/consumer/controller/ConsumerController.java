package kernel.maidlab.api.consumer.controller;

import kernel.maidlab.common.dto.consumer.ConsumerMyPageDto;
import kernel.maidlab.common.dto.consumer.request.ConsumerProfileRequestDto;
import kernel.maidlab.common.dto.consumer.request.ConsumerProfileUpdateRequestDto;
import kernel.maidlab.common.dto.consumer.response.ConsumerProfileResponseDto;
import kernel.maidlab.api.consumer.service.ConsumerService;
import kernel.maidlab.common.dto.ResponseDto;

import jakarta.servlet.http.HttpServletRequest;
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
	public ResponseEntity<ResponseDto<ConsumerMyPageDto>> getMyPage(HttpServletRequest req) {
		ConsumerMyPageDto myPageDto = consumerService.getConsumerMyPage(req);
		return ResponseDto.success(myPageDto);
	}

	@GetMapping("/profile")
	public ResponseEntity<ResponseDto<ConsumerProfileResponseDto>> getProfile(HttpServletRequest req) {
		ConsumerProfileResponseDto responseDto = consumerService.getConsumerProfile(req);
		return ResponseDto.success(responseDto);
	}

	@PostMapping("/profile")
	public ResponseEntity<ResponseDto<Void>> createProfile(
		@Validated @RequestBody ConsumerProfileRequestDto consumerProfileRequestDto,
		HttpServletRequest req) {
		consumerService.createConsumerProfile(consumerProfileRequestDto, req);
		return ResponseDto.success();
	}

	@PatchMapping("/profile")
	public ResponseEntity<ResponseDto<Void>> updateProfile(
			@Validated @RequestBody ConsumerProfileUpdateRequestDto consumerProfileUpdateRequestDto,
			HttpServletRequest req
	){
		consumerService.updateConsumerProfile(consumerProfileUpdateRequestDto, req);
		return ResponseDto.success();
	}


	@GetMapping("/likes")
	public ResponseEntity<ResponseDto<Object>> getLikes(HttpServletRequest req) {
		var likedManagers = consumerService.getLikedManagerList(req);
		return ResponseDto.success(likedManagers);
	}

	@GetMapping("/blacklists")
	public ResponseEntity<ResponseDto<Object>> getBlackListedManagerList(HttpServletRequest req) {
		var blacklistedManagers = consumerService.getBlackListedManagerList(req);
		return ResponseDto.success(blacklistedManagers);
	}

	@PostMapping("/preference/{managerUuid}")
	public ResponseEntity<ResponseDto<Void>> setManagerPreference(
		@PathVariable String managerUuid,
		@RequestParam boolean preference,
		HttpServletRequest req) {
		consumerService.saveLikedOrBlackListedManager(req, managerUuid, preference);
		return ResponseDto.success();
	}

	@DeleteMapping("/preference/{managerUuid}")
	public ResponseEntity<ResponseDto<String>> deleteManagerPreference(
			@PathVariable String managerUuid,
			HttpServletRequest req) {
		consumerService.deleteLikedAOrBlackListManager(managerUuid, req);
		return ResponseDto.success("삭제 완료");
	}
}
