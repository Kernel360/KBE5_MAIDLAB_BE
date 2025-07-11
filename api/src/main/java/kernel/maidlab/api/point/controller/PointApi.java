package kernel.maidlab.api.point.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import kernel.maidlab.common.dto.ResponseDto;
import kernel.maidlab.common.dto.point.request.PointChargeRequestDto;
import kernel.maidlab.common.dto.point.request.PointRecordRequestDto;
import kernel.maidlab.common.dto.point.response.PageResponseDto;
import kernel.maidlab.common.dto.point.response.PointRecordResponseDto;
import kernel.maidlab.common.dto.point.response.PointResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
