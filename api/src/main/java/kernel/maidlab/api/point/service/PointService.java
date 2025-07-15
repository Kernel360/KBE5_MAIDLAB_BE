package kernel.maidlab.api.point.service;

import jakarta.servlet.http.HttpServletRequest;
import kernel.maidlab.api.point.dto.request.PointChargeRequestDto;
import kernel.maidlab.api.point.dto.request.PointRecordRequestDto;
import kernel.maidlab.api.point.dto.response.PageResponseDto;
import kernel.maidlab.api.point.dto.response.PointRecordResponseDto;
import kernel.maidlab.api.point.dto.response.PointResponseDto;

public interface PointService {

	PointResponseDto getPoint(HttpServletRequest request);

	PageResponseDto<PointRecordResponseDto> getPointRecordList(HttpServletRequest request,
															   PointRecordRequestDto pointRecordRequestDto);

	void chargePoint(HttpServletRequest request,
		PointChargeRequestDto pointChargeRequestDto);
}
