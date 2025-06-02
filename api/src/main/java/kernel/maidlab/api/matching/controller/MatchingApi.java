package kernel.maidlab.api.matching.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import kernel.maidlab.api.matching.dto.response.AvailableManagerResponseDto;
import kernel.maidlab.api.matching.dto.response.MatchingResponseDto;
import kernel.maidlab.api.matching.dto.request.MatchingRequestDto;
import kernel.maidlab.common.dto.ResponseDto;

@Tag(name = "Matching", description = "매칭 관련 API")
public interface MatchingApi {
	@Operation(summary = "매칭 조회", description = "자신에게 신청된 매칭을 조회합니다.")
	@ApiResponses({@ApiResponse(responseCode = "200", description = "조회 성공"),
		@ApiResponse(responseCode = "401", description = "비로그인 접속"),
		@ApiResponse(responseCode = "403", description = "권한 없음"),
		@ApiResponse(responseCode = "500", description = "데이터베이스 오류")})
	ResponseEntity<ResponseDto<List<MatchingResponseDto>>> getMatching(HttpServletRequest request);

	@Operation(summary = "매칭 매니저 조회", description = "choosemanager의 boolean값에 따라 직접 선택할지, 자동으로 지정될지 나누어서 실행됩니다.")
	@ApiResponses({@ApiResponse(responseCode = "200", description = "조회 성공"),
	@ApiResponse(responseCode = "400", description = "알맞는 매니저가 없습니다."),
		@ApiResponse(responseCode = "401", description = "비로그인 접속"),
		@ApiResponse(responseCode = "403", description = "권한 없음"),
		@ApiResponse(responseCode = "500", description = "데이터베이스 오류")})
	ResponseEntity<List<AvailableManagerResponseDto>> matchManagers(@RequestBody MatchingRequestDto dto);

	@Operation(summary = "매칭시작", description = "매칭 테이블에 reservationid, status, managerid 값들로 row 생성")
	@ApiResponses({@ApiResponse(responseCode = "200", description = "테이블 생성 성공"),
		@ApiResponse(responseCode = "400", description = "이미 존재하는 reservationid 입니다."),
		@ApiResponse(responseCode = "401", description = "비로그인 접속"),
		@ApiResponse(responseCode = "403", description = "권한 없음"),
		@ApiResponse(responseCode = "500", description = "데이터베이스 오류")})
	ResponseEntity<ResponseDto<String>> matchStart(@RequestParam Long reservation_id, Long manager_id);

}
