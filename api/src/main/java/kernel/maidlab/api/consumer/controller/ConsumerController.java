package kernel.maidlab.api.consumer.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import kernel.maidlab.api.auth.entity.Consumer;
import kernel.maidlab.api.auth.jwt.JwtDto;
import kernel.maidlab.api.auth.jwt.JwtProvider;
import kernel.maidlab.api.consumer.dto.ConsumerMyPageDto;
import kernel.maidlab.api.consumer.dto.request.ConsumerProfileRequestDto;
import kernel.maidlab.api.consumer.dto.request.PreferenceRequestDto;
import kernel.maidlab.api.consumer.dto.response.BlackListedManagerResponseDto;
import kernel.maidlab.api.consumer.dto.response.ConsumerProfileResponseDto;
import kernel.maidlab.api.consumer.dto.response.LikedManagerResponseDto;
import kernel.maidlab.api.consumer.service.ConsumerService;
import kernel.maidlab.common.dto.ResponseDto;
import kernel.maidlab.common.enums.ResponseType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/consumers")
@RequiredArgsConstructor
public class ConsumerController implements ConsumerApi {

	private final ConsumerService consumerService;
	private final JwtProvider jwtProvider;

	// 프로필
	@Override
	@PatchMapping("/profile")
	public ResponseEntity<ResponseDto<Void>> updateProfile(HttpServletRequest request,
		@RequestBody ConsumerProfileRequestDto consumerProfileRequestDto) {
		String uuid = getUuidByToken(request);
		Consumer findedConsumer = consumerService.getConsumer(uuid);
		consumerService.updateConsumerProfile(findedConsumer, consumerProfileRequestDto);
		return ResponseDto.success(ResponseType.SUCCESS, null);
	}

	// 조회
	@Override
	@GetMapping("/profile")
	public ResponseEntity<ResponseDto<ConsumerProfileResponseDto>> getProfile(HttpServletRequest request) {

		String uuid = getUuidByToken(request);
		Consumer findedConsumer = consumerService.getConsumer(uuid);
		ConsumerProfileResponseDto consumerProfileResponseDto = ConsumerProfileResponseDto.builder()
			.profileImage(findedConsumer.getProfileImage())
			.phoneNumber(findedConsumer.getPhoneNumber())
			.name(findedConsumer.getName())
			.birth(findedConsumer.getBirth())
			.gender(findedConsumer.getGender())
			.address(findedConsumer.getAddress())
			.detailAddress(findedConsumer.getDetailAddress())
			.build();

		return ResponseDto.success(ResponseType.SUCCESS, consumerProfileResponseDto);
	}

	@Override
	@GetMapping("/mypage")
	public ResponseEntity<ResponseDto<ConsumerMyPageDto>> getMypage(HttpServletRequest request) {

		String uuid = getUuidByToken(request);
		Consumer findedConsumer = consumerService.getConsumer(uuid);
		ConsumerMyPageDto myPageDto = ConsumerMyPageDto.builder()
			.name(findedConsumer.getName())
			.point(findedConsumer.getPoint())
			.profileImage(findedConsumer.getProfileImage())
			.build();

		return ResponseDto.success(ResponseType.SUCCESS, myPageDto);
	}

	// 찜한 매니저 조회
	@Override
	@GetMapping("/likes")
	public ResponseEntity<ResponseDto<List<LikedManagerResponseDto>>> getLikeManagers(HttpServletRequest request) {

		String uuid = getUuidByToken(request);
		Consumer consumer = consumerService.getConsumer(uuid);
		List<LikedManagerResponseDto> likedManagerList = consumerService.getLikeManagerList(consumer);
		return ResponseDto.success(ResponseType.SUCCESS, likedManagerList);
	}

	// 블랙리스트 매니저 조회
	@Override
	@GetMapping("/blacklists")
	public ResponseEntity<ResponseDto<List<BlackListedManagerResponseDto>>> getBlackListManagers(
		HttpServletRequest request) {

		String uuid = getUuidByToken(request);
		Consumer consumer = consumerService.getConsumer(uuid);
		List<BlackListedManagerResponseDto> blackListedManagerList = consumerService.getBlackListedManagerList(
			consumer);
		return ResponseDto.success(ResponseType.SUCCESS, blackListedManagerList);
	}

	// 찜/블랙리스트 매니저 등록
	@Override
	@PostMapping("/preference/{managerUuid}")
	public ResponseEntity<ResponseDto<Void>> createLikedOrBlackListedManager(HttpServletRequest request,
		@PathVariable("managerUuid") String managerUuid,
		@RequestBody @Valid PreferenceRequestDto preferenceRequestDto) {

		String consumerUuid = getUuidByToken(request);
		boolean preference = preferenceRequestDto.isPreference();
		log.info("{}", preference);
		log.info("uuid 값 확인 = {}", managerUuid);

		consumerService.saveLikedOrBlackListedManager(consumerUuid, managerUuid, preference);
		return ResponseDto.success(ResponseType.SUCCESS, null);
	}

	// 찜한 매니저 삭제
	@Override
	@DeleteMapping("/likes/{managerUuid}")
	public ResponseEntity<ResponseDto<Void>> removeLikedManager(HttpServletRequest request,
		@PathVariable("managerUuid") String managerUuid) {

		String consumerUuid = getUuidByToken(request);
		log.info("수요자 uuid : {}", consumerUuid);
		log.info("매니저 uuid : {}", managerUuid);
		long deletedCount = consumerService.deleteLikedManager(consumerUuid, managerUuid);

		if (deletedCount != 1) {
			throw new IllegalStateException("매니저 찜 또는 블랙리스트 항목 삭제에 실패했습니다. 삭제 대상이 존재하지 않거나 중복입니다.");
		}

		return ResponseDto.success(null);
	}

	private String getUuidByToken(HttpServletRequest request) {

		String token = jwtProvider.extractToken(request);
		JwtDto.ValidationResult jwtResult = jwtProvider.validateAccessToken(token);
		return jwtResult.getUuid();
	}
}
