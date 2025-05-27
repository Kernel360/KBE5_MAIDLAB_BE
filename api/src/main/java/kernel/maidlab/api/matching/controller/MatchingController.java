package kernel.maidlab.api.matching.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import kernel.maidlab.api.matching.dto.ManagerResponseDto;
import kernel.maidlab.api.matching.dto.MatchingRequestDto;

public interface MatchingController {

	ResponseEntity<List<ManagerResponseDto>> MatchManagers(@RequestBody MatchingRequestDto dto);

}
