package kernel.maidlab.api.point.service;

import jakarta.servlet.http.HttpServletRequest;
import kernel.maidlab.common.dto.point.request.PointRecordRequestDto;
import kernel.maidlab.common.dto.point.response.PageResponseDto;
import kernel.maidlab.common.dto.point.response.PointRecordResponseDto;
import kernel.maidlab.common.dto.point.response.PointResponseDto;

public interface PointService {

    PointResponseDto getPoint(HttpServletRequest request);

    PageResponseDto<PointRecordResponseDto> getPointRecordList(HttpServletRequest request,
                                                               PointRecordRequestDto pointRecordRequestDto);
}
