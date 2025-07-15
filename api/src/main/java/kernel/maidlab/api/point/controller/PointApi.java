package kernel.maidlab.api.point.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import kernel.maidlab.api.point.dto.request.PointChargeRequestDto;
import kernel.maidlab.api.point.dto.request.PointRecordRequestDto;
import kernel.maidlab.api.point.dto.response.PageResponseDto;
import kernel.maidlab.api.point.dto.response.PointRecordResponseDto;
import kernel.maidlab.api.point.dto.response.PointResponseDto;
import kernel.maidlab.common.dto.ResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "Point", description = "포인트 관련 API")
public interface PointApi {
	@Operation(summary = "수요자 현재 포인트 조회", description = "현재 로그인한 수요자의 사용 가능 포인트를 조회합니다.")
	ResponseEntity<ResponseDto<PointResponseDto>> getPoint(
		@Parameter(hidden = true) HttpServletRequest request
	);

	@Operation(summary = "수요자 포인트 이력 조회", description = "현재 로그인한 수요자의 포인트 사용 및 적립 이력을 조회합니다.")
	ResponseEntity<ResponseDto<PageResponseDto<PointRecordResponseDto>>> getPointRecord(
		@Parameter(hidden = true) HttpServletRequest request,
		@RequestBody PointRecordRequestDto pointRecordRequestDto
	);

	@Operation(summary = "포인트 충전", description = "수요자가 포인트를 충전합니다.")
	ResponseEntity<ResponseDto<String>> chargePoint(
		@Parameter(hidden = true) HttpServletRequest request,
		@RequestBody PointChargeRequestDto pointChargeRequestDto
	);
}
