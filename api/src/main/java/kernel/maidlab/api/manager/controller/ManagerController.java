package kernel.maidlab.api.manager.controller;

import kernel.maidlab.api.manager.dto.request.*;
import kernel.maidlab.api.manager.dto.response.*;
import kernel.maidlab.api.manager.service.ManagerService;
import kernel.maidlab.common.dto.ResponseDto;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/manager")
@RequiredArgsConstructor
public class ManagerController implements ManagerApi {

	private final ManagerService managerService;

	@Override
	@PostMapping("/profile")
	public ResponseEntity<ResponseDto<Void>> createProfile(@Validated @RequestBody ProfileRequestDto req,
		HttpServletRequest httpReq) {

		return managerService.createProfile(req, httpReq);
	}

	@Override
	@GetMapping("/mypage")
	public ResponseEntity<ResponseDto<MypageResponseDto>> getMypage(HttpServletRequest req) {

		return managerService.getMypage(req);
	}

	@Override
	@GetMapping("/profile")
	public ResponseEntity<ResponseDto<ProfileResponseDto>> getProfile(HttpServletRequest req) {

		return managerService.getProfile(req);
	}

	@Override
	@PutMapping("/profile")
	public ResponseEntity<ResponseDto<Void>> updateProfile(@Validated @RequestBody ProfileUpdateRequestDto req,
		HttpServletRequest httpReq) {

		return managerService.updateProfile(req, httpReq);
	}

	@Override
	@GetMapping("/myReviews")
	public ResponseEntity<ResponseDto<ReviewListResponseDto>> getMyReviews(HttpServletRequest req) {

		return managerService.getMyReviews(req);
	}

}