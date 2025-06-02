package kernel.maidlab.admin.matching.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestParam;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import kernel.maidlab.api.matching.dto.response.MatchingResponseDto;
import kernel.maidlab.common.dto.ResponseDto;
import kernel.maidlab.common.enums.Status;

@Tag(name = "Matching", description = "매칭 관련 API")
public interface AdminMatchingApi {
	@Operation(summary = "매칭 조회", description = "현재 매칭 테이블에 저장되어있는 모든 매칭 조회")
	@ApiResponses({@ApiResponse(responseCode = "200", description = "조회 성공"),
		@ApiResponse(responseCode = "401", description = "비로그인 접속"),
		@ApiResponse(responseCode = "403", description = "권한 없음"),
		@ApiResponse(responseCode = "500", description = "데이터베이스 오류")})
	ResponseEntity<ResponseDto<List<MatchingResponseDto>>> allMatching(HttpServletRequest request);

	@Operation(summary = "매칭 상태별 조회", description = "현재 매칭 테이블에 저장되어있는 모든 매칭 조회")
	@ApiResponses({@ApiResponse(responseCode = "200", description = "조회 성공"),
		@ApiResponse(responseCode = "401", description = "비로그인 접속"),
		@ApiResponse(responseCode = "403", description = "권한 없음"),
		@ApiResponse(responseCode = "500", description = "데이터베이스 오류")})
	ResponseEntity<ResponseDto<List<MatchingResponseDto>>> statusMatching(@RequestParam Status status, HttpServletRequest request);

	@Operation(summary = "매칭 매니저 변경", description = "매칭된 매니저가 아닌 다른 매니저로 다시 매칭(강제)")
	@ApiResponses({@ApiResponse(responseCode = "200", description = "조회 성공"),
		@ApiResponse(responseCode = "401", description = "비로그인 접속"),
		@ApiResponse(responseCode = "403", description = "권한 없음"),
		@ApiResponse(responseCode = "500", description = "데이터베이스 오류")})
	ResponseEntity<ResponseDto<String>> managerChange(@RequestParam Long reservationId,
		@RequestParam Long managerId);
}
