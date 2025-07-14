package kernel.maidlab.api.manager.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.ResponseEntity;

import jakarta.servlet.http.HttpServletRequest;
import kernel.maidlab.common.dto.ResponseDto;
import kernel.maidlab.common.dto.manager.request.ProfileRequestDto;
import kernel.maidlab.common.dto.manager.request.ProfileUpdateRequestDto;
import kernel.maidlab.common.dto.manager.response.MypageResponseDto;
import kernel.maidlab.common.dto.manager.response.ProfileResponseDto;
import kernel.maidlab.common.dto.manager.response.ReviewListResponseDto;
import kernel.maidlab.common.dto.matching.response.AvailableManagerResponseDto;
import kernel.maidlab.common.entity.consumer.Consumer;
import kernel.maidlab.common.entity.manager.Manager;

public interface ManagerService {

	ResponseEntity<ResponseDto<Void>> createProfile(ProfileRequestDto req, HttpServletRequest httpReq);

	ResponseEntity<ResponseDto<MypageResponseDto>> getMypage(HttpServletRequest req);

	ResponseEntity<ResponseDto<ProfileResponseDto>> getProfile(HttpServletRequest req);

	ResponseEntity<ResponseDto<Void>> updateProfile(ProfileUpdateRequestDto req, HttpServletRequest httpReq);

	ResponseEntity<ResponseDto<ReviewListResponseDto>> getMyReviews(HttpServletRequest req);

	List<AvailableManagerResponseDto> findAvailableManagers(String gu, LocalDateTime StartTime, LocalDateTime EndTime);

	List<AvailableManagerResponseDto> previousManagers(Consumer consumer);

	kernel.maidlab.common.entity.manager.Manager getManager(String userId);

	Manager findById(Long managerId);
}
