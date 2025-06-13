package kernel.maidlab.api.consumer.controller;

import kernel.maidlab.api.consumer.dto.request.ConsumerProfileRequestDto;
import kernel.maidlab.api.consumer.dto.response.ConsumerProfileResponseDto;
import kernel.maidlab.api.consumer.service.ConsumerService;
import kernel.maidlab.api.auth.jwt.JwtFilter;
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

	@GetMapping("/profile")
	public ResponseEntity<ResponseDto<ConsumerProfileResponseDto>> getProfile(HttpServletRequest req) {
		String uuid = (String) req.getAttribute(JwtFilter.CURRENT_USER_UUID_KEY);

		ConsumerProfileResponseDto responseDto = consumerService.getConsumerProfile(uuid);

		return ResponseDto.success(responseDto);
	}

	@PatchMapping("/profile")
	public ResponseEntity<ResponseDto<Void>> updateProfile(
		@Validated @RequestBody ConsumerProfileRequestDto req,
		HttpServletRequest httpReq) {

		String uuid = (String) httpReq.getAttribute(JwtFilter.CURRENT_USER_UUID_KEY);

		consumerService.updateConsumerProfile(uuid, req);

		return ResponseDto.success();
	}

	@GetMapping("/mypage")
	public ResponseEntity<ResponseDto<Object>> getMypage(HttpServletRequest req) {
		String uuid = (String) req.getAttribute(JwtFilter.CURRENT_USER_UUID_KEY);

		Object mypageData = consumerService.getMypage(uuid);

		return ResponseDto.success(mypageData);
	}

	@GetMapping("/likes")
	public ResponseEntity<ResponseDto<Object>> getLikes(HttpServletRequest req) {
		String uuid = (String) req.getAttribute(JwtFilter.CURRENT_USER_UUID_KEY);

		var likedManagers = consumerService.getLikedManagers(uuid);

		return ResponseDto.success(likedManagers);
	}

	@GetMapping("/blacklists")
	public ResponseEntity<ResponseDto<Object>> getBlacklists(HttpServletRequest req) {
		String uuid = (String) req.getAttribute(JwtFilter.CURRENT_USER_UUID_KEY);

		var blacklistedManagers = consumerService.getBlacklistedManagers(uuid);

		return ResponseDto.success(blacklistedManagers);
	}

	@PostMapping("/preference/{managerUuid}")
	public ResponseEntity<ResponseDto<Void>> setManagerPreference(
		@RequestParam String managerUuid,
		@RequestParam boolean preference,
		HttpServletRequest req) {

		String uuid = (String) req.getAttribute(JwtFilter.CURRENT_USER_UUID_KEY);

		consumerService.setManagerPreference(uuid, managerUuid, preference);

		return ResponseDto.success();
	}

	@DeleteMapping("/likes/{managerUuid}")
	public ResponseEntity<ResponseDto<Void>> deleteLikedManager(
		@PathVariable String managerUuid,
		HttpServletRequest req) {

		String uuid = (String) req.getAttribute(JwtFilter.CURRENT_USER_UUID_KEY);

		consumerService.deleteLikedManager(uuid, managerUuid);

		return ResponseDto.success();
	}
}