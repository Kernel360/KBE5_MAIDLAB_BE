package kernel.maidlab.api.point.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import kernel.maidlab.api.point.service.PointService;
import kernel.maidlab.common.dto.ResponseDto;
import kernel.maidlab.common.dto.point.request.PointChargeRequestDto;
import kernel.maidlab.common.dto.point.request.PointRecordRequestDto;
import kernel.maidlab.common.dto.point.response.PageResponseDto;
import kernel.maidlab.common.dto.point.response.PointRecordResponseDto;
import kernel.maidlab.common.dto.point.response.PointResponseDto;
import kernel.maidlab.common.enums.UserType;
import kernel.maidlab.core.aop.annotation.auth.AuthRequired;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/point")
public class PointController {

	private final PointService pointService;

	// 수요자 현재 포인트 조회
	@GetMapping
	@AuthRequired(roles = {UserType.CONSUMER})
	public ResponseEntity<ResponseDto<PointResponseDto>> getPoint(HttpServletRequest request) {
		PointResponseDto point = pointService.getPoint(request);
		return ResponseDto.success(point);
	}

	// 수요자 포인트 이력 조회
	@PostMapping("/record")
	@AuthRequired(roles = {UserType.CONSUMER})
	public ResponseEntity<ResponseDto<PageResponseDto<PointRecordResponseDto>>> getPointRecord(
		HttpServletRequest request,
		@RequestBody PointRecordRequestDto pointRecordRequestDto) {

		PageResponseDto<PointRecordResponseDto> pointRecordList =
			pointService.getPointRecordList(request, pointRecordRequestDto);
		return ResponseDto.success(pointRecordList);
	}

	// 포인트 충전
	@PostMapping("/charge")
	public ResponseEntity<ResponseDto<String>> chargePoint(
		HttpServletRequest request,
		@RequestBody PointChargeRequestDto pointChargeRequestDto
	) {
		pointService.chargePoint(request, pointChargeRequestDto);
		return ResponseDto.success("충전이 완료 되었습니다.");
	}
}
