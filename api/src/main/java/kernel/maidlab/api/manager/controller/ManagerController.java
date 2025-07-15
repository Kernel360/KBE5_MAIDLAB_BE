package kernel.maidlab.api.manager.controller;

import jakarta.servlet.http.HttpServletRequest;
import kernel.maidlab.api.manager.dto.request.ProfileRequestDto;
import kernel.maidlab.api.manager.dto.request.ProfileUpdateRequestDto;
import kernel.maidlab.api.manager.dto.response.MypageResponseDto;
import kernel.maidlab.api.manager.dto.response.ProfileResponseDto;
import kernel.maidlab.api.manager.dto.response.ReviewListResponseDto;
import kernel.maidlab.api.manager.service.ManagerService;
import kernel.maidlab.common.dto.ResponseDto;
import kernel.maidlab.common.enums.UserType;
import kernel.maidlab.core.aop.annotation.auth.AuthRequired;
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
	@AuthRequired(roles = {UserType.MANAGER})
	public ResponseEntity<ResponseDto<Void>> createProfile(@Validated @RequestBody ProfileRequestDto req,
		HttpServletRequest httpReq) {
		return managerService.createProfile(req, httpReq);
	}

	@Override
	@GetMapping("/mypage")
	@AuthRequired(roles = {UserType.MANAGER})
	public ResponseEntity<ResponseDto<MypageResponseDto>> getMypage(HttpServletRequest req) {
		return managerService.getMypage(req);
	}

	@Override
	@GetMapping("/profile")
	@AuthRequired(roles = {UserType.MANAGER})
	public ResponseEntity<ResponseDto<ProfileResponseDto>> getProfile(HttpServletRequest req) {
		return managerService.getProfile(req);
	}

	@Override
	@PutMapping("/profile")
	@AuthRequired(roles = {UserType.MANAGER})
	public ResponseEntity<ResponseDto<Void>> updateProfile(@Validated @RequestBody ProfileUpdateRequestDto req,
		HttpServletRequest httpReq) {
		return managerService.updateProfile(req, httpReq);
	}

	@Override
	@GetMapping("/myReviews")
	@AuthRequired(roles = {UserType.MANAGER})
	public ResponseEntity<ResponseDto<ReviewListResponseDto>> getMyReviews(HttpServletRequest req) {
		return managerService.getMyReviews(req);
	}

}
