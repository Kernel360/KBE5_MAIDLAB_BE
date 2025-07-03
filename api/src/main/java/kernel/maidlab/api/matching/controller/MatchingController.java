package kernel.maidlab.api.matching.controller;

import java.util.Collections;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import kernel.maidlab.api.consumer.service.ConsumerService;
import kernel.maidlab.common.dto.consumer.response.LikedManagerResponseDto;
import kernel.maidlab.common.dto.matching.response.RequestMatchingListResponseDto;
import kernel.maidlab.common.entity.consumer.Consumer;
import kernel.maidlab.common.exception.BaseException;
import kernel.maidlab.common.dto.matching.response.AvailableManagerResponseDto;
import kernel.maidlab.common.dto.matching.request.MatchingRequestDto;
import kernel.maidlab.common.dto.matching.response.MatchingResponseDto;
import kernel.maidlab.api.matching.service.MatchingService;
import kernel.maidlab.common.dto.ResponseDto;
import kernel.maidlab.common.enums.ResponseType;
import kernel.maidlab.common.enums.Status;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/matching")
@RestController
public class MatchingController implements MatchingApi {

	// private final AuthUtil authUtil;
	private final MatchingService matchingService;
	private final ConsumerService consumerService;
	// private final MatchingRepository matchingRepository;
	// private final RedisTemplate<String, Object> redisTemplate;
	// private final RedisService redisService;

	@GetMapping
	@Override
	public ResponseEntity<ResponseDto<List<RequestMatchingListResponseDto>>> getMatching(HttpServletRequest request,
		@RequestParam int page, @RequestParam int size) {
		List<RequestMatchingListResponseDto> response = matchingService.myMatching(request, page, size);
		return ResponseDto.success(ResponseType.SUCCESS, response);
	}

	@PostMapping("/matchmanager")
	@Override
	public ResponseEntity<ResponseDto<List<AvailableManagerResponseDto>>> matchManagers(@RequestBody MatchingRequestDto dto) {
		List<AvailableManagerResponseDto> AvailableManagers = matchingService.findAvailableManagers(dto);

		// 후보군 작성을 위한 redis 설정으로 일단 일시중지
		//int ttlMin = AvailableManagers.size() * 10;
		//Object key = generateKey(dto);
		//redisService.saveManagerList(key.toString(), AvailableManagers, ttlMin);
		if (AvailableManagers.isEmpty()) {
			throw new BaseException(ResponseType.AVAILABLE_MANAGER_DOES_NOT_EXIST);
		}

		if (dto.getManagerChoose())
			return ResponseDto.success(ResponseType.SUCCESS, AvailableManagers);
			// return ResponseEntity.ok(AvailableManagers);
		else
			// return ResponseEntity.ok(Collections.singletonList(AvailableManagers.getFirst()));
			return ResponseDto.success(ResponseType.SUCCESS, Collections.singletonList(AvailableManagers.getFirst()));
	}

	@GetMapping("/preferencemanager")
	@Override
	public ResponseEntity<ResponseDto<List<LikedManagerResponseDto>>> preferenceManager(HttpServletRequest request) {
		List<LikedManagerResponseDto> response = matchingService.preferenceManager(request);
		return ResponseDto.success(ResponseType.SUCCESS, response);
	}

	@GetMapping("/previousmanager")
	@Override
	public ResponseEntity<ResponseDto<List<AvailableManagerResponseDto>>> previousManager(HttpServletRequest request) {
		Consumer consumer = consumerService.getConsumer(request);
		List<AvailableManagerResponseDto> response = matchingService.previousManager(consumer);
		return ResponseDto.success(ResponseType.SUCCESS, response);
	}

	@PostMapping("/matchstart")
	@Override
	public ResponseEntity<ResponseDto<String>> matchStart(@RequestParam Long reservation_id,
		@RequestParam Long manager_id) {
		MatchingResponseDto matchingResponseDto = MatchingResponseDto.builder()
			.managerId(manager_id)
			.reservationId(reservation_id)
			.matchingStatus(Status.PENDING)
			.build();
		matchingService.createMatching(matchingResponseDto);
		return ResponseDto.success(ResponseType.SUCCESS, matchingResponseDto.toString());
	}


}
