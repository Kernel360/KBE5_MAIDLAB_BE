package kernel.maidlab.api.point.controller;

import jakarta.servlet.http.HttpServletRequest;
import kernel.maidlab.api.point.service.PointService;
import kernel.maidlab.common.dto.ResponseDto;
import kernel.maidlab.common.dto.point.request.PointRecordRequestDto;
import kernel.maidlab.common.dto.point.response.PageResponseDto;
import kernel.maidlab.common.dto.point.response.PointRecordResponseDto;
import kernel.maidlab.common.dto.point.response.PointResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/point")
@RequiredArgsConstructor
public class PointController {

    private final PointService pointService;

    // 수요자 현재 포인트 조회
    @GetMapping
    public ResponseEntity<ResponseDto<PointResponseDto>> getPoint(HttpServletRequest request){
        PointResponseDto point = pointService.getPoint(request);
        return ResponseDto.success(point);
    }

    // 수요자 포인트 이력 조회
    @PostMapping("/record")
    public ResponseEntity<ResponseDto<PageResponseDto<PointRecordResponseDto>>> getPointRecord(
            HttpServletRequest request,
            @RequestBody PointRecordRequestDto pointRecordRequestDto){

        PageResponseDto<PointRecordResponseDto> pointRecordList = pointService.getPointRecordList(request, pointRecordRequestDto);

        return ResponseDto.success(pointRecordList);
    }
}
