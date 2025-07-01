package kernel.maidlab.api.manager.controller;

import kernel.maidlab.common.dto.manager.request.*;
import kernel.maidlab.common.dto.manager.response.*;
import kernel.maidlab.api.manager.service.ManagerService;
import kernel.maidlab.common.dto.ResponseDto;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/manager")
@RequiredArgsConstructor
public class ManagerController implements ManagerApi {

	private final ManagerService managerService;

	@Override
	@PostMapping("/profile")
	public ResponseEntity<ResponseDto<Void>> createProfile(@Validated @RequestBody ProfileRequestDto req,
		HttpServletRequest httpReq) {
		log.info("Create manager profile request received");
		return managerService.createProfile(req, httpReq);
	}

	@Override
	@GetMapping("/mypage")
	public ResponseEntity<ResponseDto<MypageResponseDto>> getMypage(HttpServletRequest req) {
		log.info("Get manager mypage request received");
		return managerService.getMypage(req);
	}

	@Override
	@GetMapping("/profile")
	public ResponseEntity<ResponseDto<ProfileResponseDto>> getProfile(HttpServletRequest req) {
		log.info("Get manager profile request received");
		return managerService.getProfile(req);
	}

	@Override
	@PutMapping("/profile")
	public ResponseEntity<ResponseDto<Void>> updateProfile(@Validated @RequestBody ProfileUpdateRequestDto req,
		HttpServletRequest httpReq) {
		log.info("Update manager profile request received");
		return managerService.updateProfile(req, httpReq);
	}

	@Override
	@GetMapping("/myReviews")
	public ResponseEntity<ResponseDto<ReviewListResponseDto>> getMyReviews(HttpServletRequest req) {
		log.info("Get manager reviews request received");
		return managerService.getMyReviews(req);
	}

}
