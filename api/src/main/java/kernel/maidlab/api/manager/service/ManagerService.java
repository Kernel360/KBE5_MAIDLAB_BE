package kernel.maidlab.api.manager.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.ResponseEntity;

import jakarta.servlet.http.HttpServletRequest;

import kernel.maidlab.common.dto.manager.request.*;
import kernel.maidlab.common.dto.manager.response.*;
import kernel.maidlab.common.dto.ResponseDto;

import org.springframework.data.domain.Page;

import jakarta.transaction.Transactional;
import kernel.maidlab.common.dto.manager.ManagerListResponseDto;
import kernel.maidlab.common.dto.manager.ManagerResponseDto;
import kernel.maidlab.common.dto.matching.response.AvailableManagerResponseDto;
import kernel.maidlab.common.enums.Status;

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

	Page<ManagerListResponseDto> getManagerByPageWithStatus(int page, int size, Status status);

	List<AvailableManagerResponseDto> findAvailableManagers(String gu, LocalDateTime StartTime, LocalDateTime EndTime);
}
