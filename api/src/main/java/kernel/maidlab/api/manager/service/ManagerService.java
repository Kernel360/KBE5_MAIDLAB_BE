package kernel.maidlab.api.manager.service;

import org.springframework.http.ResponseEntity;

import jakarta.servlet.http.HttpServletRequest;

import kernel.maidlab.api.manager.dto.request.*;
import kernel.maidlab.api.manager.dto.response.*;
import kernel.maidlab.common.dto.ResponseDto;

import org.springframework.data.domain.Page;

import jakarta.transaction.Transactional;
import kernel.maidlab.api.manager.dto.ManagerListResponseDto;
import kernel.maidlab.api.manager.dto.ManagerResponseDto;

public interface ManagerService {

	ResponseEntity<ResponseDto<Void>> createProfile(ProfileRequestDto req, HttpServletRequest httpReq);

	ResponseEntity<ResponseDto<MypageResponseDto>> getMypage(HttpServletRequest req);

	ResponseEntity<ResponseDto<ProfileResponseDto>> getProfile(HttpServletRequest req);

	Page<ManagerListResponseDto> getManagerBypage(int page, int size);

	ManagerResponseDto getManager(Long id);

	void approveManager(Long managerId);

	@Transactional
	void rejectManager(Long managerId);
	// Long GetIdByUuid(String uuid);

	ResponseEntity<ResponseDto<Void>> updateProfile(ProfileUpdateRequestDto req, HttpServletRequest httpReq);

	ResponseEntity<ResponseDto<ReviewListResponseDto>> getMyReviews(HttpServletRequest req);
}
