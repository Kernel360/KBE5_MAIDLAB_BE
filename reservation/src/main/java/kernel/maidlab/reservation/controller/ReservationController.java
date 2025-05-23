package kernel.maidlab.reservation.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import kernel.maidlab.common.dto.ResponseDto;
import kernel.maidlab.common.dto.ResponseType;
import kernel.maidlab.common.dto.baseResponse.BaseResponse;
import kernel.maidlab.common.dto.baseResponse.ResponseCode;
import kernel.maidlab.reservation.dto.response.AvailableManagerDto;
import kernel.maidlab.reservation.service.ReservationService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reservation")
public class ReservationController implements ReservationApi{
	private final ReservationService reservationService;

	@Override
	@GetMapping("/managers")
	public ResponseEntity<ResponseDto<List<AvailableManagerDto>>> getAvailableManagers(
		@RequestParam LocalDateTime startTime
	) {
		List<AvailableManagerDto> availableManagerList = reservationService.getAvailableManagersList(startTime);
		return ResponseDto.success(ResponseType.SUCCESS,availableManagerList);
	}
}

//
// public ResponseEntity<BaseResponse<ManagerProfile>> getProfile(@RequestHeader("Authorization") String authHeader) {
// 	String phone = jwtUtil.getPhoneFromToken(authHeader.replace("Bearer ", ""));
//
// 	if ("010-1111-1111".equals(phone)) {
// 		ManagerProfile mockProfile = new ManagerProfile("홍길동", "강남구");
// 		return ResponseEntity.ok(BaseResponse.success(mockProfile));
// 	}
//
// 	return ResponseEntity.ok(BaseResponse.error(ResponseCode.AF));
// }