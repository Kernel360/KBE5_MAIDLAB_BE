package kernel.maidlab.api.matching.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kernel.maidlab.api.matching.dto.ManagerResponseDto;
import kernel.maidlab.api.matching.dto.MatchingRequestDto;
import kernel.maidlab.api.matching.service.MatchingService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RequestMapping("/api/matching")
@RestController
public class MatchingControllerImpl implements MatchingController {

	private final MatchingService matchingService;

	@PostMapping("/matchstart")
	@Override
	public ResponseEntity<List<ManagerResponseDto>> MatchManagers(@RequestBody MatchingRequestDto dto) {
		System.out.println("startTime = " + dto.getStartTime());
		System.out.println("endTime = " + dto.getEndTime());
		System.out.println("address = " + dto.getAddress());
		List<ManagerResponseDto> AvailableManagers = matchingService.FindAvailableManagers(dto);
		System.out.println(AvailableManagers.getFirst());
		return ResponseEntity.ok(AvailableManagers);
	}
}
