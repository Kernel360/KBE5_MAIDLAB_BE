package kernel.maidlab.api.consumer.controller;

import kernel.maidlab.api.auth.entity.Consumer;
import kernel.maidlab.api.consumer.dto.ConsumerMyPageDto;
import kernel.maidlab.api.consumer.dto.request.ConsumerProfileRequestDto;
import kernel.maidlab.api.consumer.dto.response.ConsumerProfileResponseDto;
import kernel.maidlab.api.consumer.service.ConsumerService;
import kernel.maidlab.api.auth.jwt.JwtFilter;
import kernel.maidlab.common.dto.ResponseDto;

import jakarta.servlet.http.HttpServletRequest;
import kernel.maidlab.common.enums.ResponseType;
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

		String uuid = (String) req.getAttribute(JwtFilter.CURRENT_USER_UUID_KEY);

		Consumer findedConsumer = consumerService.getConsumerByUuid(uuid);
		ConsumerMyPageDto myPageDto = ConsumerMyPageDto.builder()
				.name(findedConsumer.getName())
				.point(findedConsumer.getPoint())
				.profileImage(findedConsumer.getProfileImage())
				.build();

		return ResponseDto.success(ResponseType.SUCCESS, myPageDto);
	}

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

	@GetMapping("/likes")
	public ResponseEntity<ResponseDto<Object>> getLikes(HttpServletRequest req) {
		String uuid = (String) req.getAttribute(JwtFilter.CURRENT_USER_UUID_KEY);

		Consumer consumer = consumerService.getConsumerByUuid(uuid); // 🔧 Consumer 객체 먼저 가져오기
		var likedManagers = consumerService.getLikeManagerList(consumer); // 🔧 수정된 호출

		return ResponseDto.success(likedManagers);
	}

	@GetMapping("/blacklists")
	public ResponseEntity<ResponseDto<Object>> getBlackListedManagerList(HttpServletRequest req) {
		String uuid = (String) req.getAttribute(JwtFilter.CURRENT_USER_UUID_KEY);

		Consumer consumer = consumerService.getConsumerByUuid(uuid); // 🔧 Consumer 객체 먼저 가져오기
		var blacklistedManagers = consumerService.getBlackListedManagerList(consumer); // 🔧 수정된 호출

		return ResponseDto.success(blacklistedManagers);
	}

	@PostMapping("/preference/{managerUuid}")
	public ResponseEntity<ResponseDto<Void>> setManagerPreference(
		@PathVariable String managerUuid, // 🔧 @RequestParam → @PathVariable
		@RequestParam boolean preference,
		HttpServletRequest req) {

		String uuid = (String) req.getAttribute(JwtFilter.CURRENT_USER_UUID_KEY);

		consumerService.saveLikedOrBlackListedManager(uuid, managerUuid, preference); // 🔧 수정된 호출

		return ResponseDto.success();
	}
}