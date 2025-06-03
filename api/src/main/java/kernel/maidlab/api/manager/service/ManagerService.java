package kernel.maidlab.api.manager.service;

import org.springframework.http.ResponseEntity;

import jakarta.servlet.http.HttpServletRequest;

import kernel.maidlab.api.manager.dto.request.*;
import kernel.maidlab.api.manager.dto.response.*;
import kernel.maidlab.common.dto.ResponseDto;

public interface ManagerService {

	ResponseEntity<ResponseDto<Void>> createProfile(ProfileRequestDto req, HttpServletRequest httpReq);

	ResponseEntity<ResponseDto<MypageResponseDto>> getMypage(HttpServletRequest req);

	ResponseEntity<ResponseDto<ProfileResponseDto>> getProfile(HttpServletRequest req);

	ResponseEntity<ResponseDto<Void>> updateProfile(ProfileUpdateRequestDto req, HttpServletRequest httpReq);

	ResponseEntity<ResponseDto<ReviewListResponseDto>> getMyReviews(HttpServletRequest req);
}