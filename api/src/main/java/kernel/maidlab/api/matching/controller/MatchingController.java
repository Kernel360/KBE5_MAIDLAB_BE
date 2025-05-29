package kernel.maidlab.api.matching.controller;

import java.util.Collections;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import kernel.maidlab.api.exception.BaseException;
import kernel.maidlab.api.matching.dto.AvailableManagerResponseDto;
import kernel.maidlab.api.matching.dto.MatchingRequestDto;
import kernel.maidlab.api.matching.dto.MatchingDto;
import kernel.maidlab.api.matching.repository.MatchingRepository;
import kernel.maidlab.api.matching.service.MatchingService;
import kernel.maidlab.common.dto.ResponseDto;
import kernel.maidlab.common.enums.ResponseType;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RequestMapping("/api/matching")
@RestController
public class MatchingController implements MatchingApi {

	private final MatchingService matchingService;
	private final MatchingRepository matchingRepository;
	// private final RedisTemplate<String, Object> redisTemplate;
	// private final RedisService redisService;

	@PostMapping("/matchmanager")
	@Override
	public ResponseEntity<List<AvailableManagerResponseDto>> MatchManagers(@RequestBody MatchingRequestDto dto) {
		System.out.println("startTime = " + dto.getStartTime());
		System.out.println("endTime = " + dto.getEndTime());
		System.out.println("address = " + dto.getAddress());
		List<AvailableManagerResponseDto> AvailableManagers = matchingService.FindAvailableManagers(dto);

		// 후보군 작성을 위한 redis 설정으로 일단 일시중지
		//int ttlMin = AvailableManagers.size() * 10;
		//Object key = generateKey(dto);
		//redisService.saveManagerList(key.toString(), AvailableManagers, ttlMin);
		if(AvailableManagers.isEmpty()) {
			throw new BaseException(ResponseType.AVAILABLE_MANAGER_DOES_NOT_EXIST);
		}

		if (dto.getManagerChoose())
			return ResponseEntity.ok(AvailableManagers);
		else
			return ResponseEntity.ok(Collections.singletonList(AvailableManagers.getFirst()));
	}

	@PostMapping("/matchstart")
	@Override
	public ResponseEntity<ResponseDto<String>> MatchStart(@RequestParam Long reservation_id,@RequestParam Long manager_id) {
		MatchingDto matchingDto = new MatchingDto();
		matchingDto.setManagerId(manager_id);
		matchingDto.setReservationId(reservation_id);
		matchingService.createMatching(matchingDto);
		return ResponseDto.success(ResponseType.SUCCESS, matchingDto.toString());
	}
}
