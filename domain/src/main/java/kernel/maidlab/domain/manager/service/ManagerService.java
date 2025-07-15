package kernel.maidlab.domain.manager.service;

import jakarta.servlet.http.HttpServletRequest;
import kernel.maidlab.domain.consumer.entity.Consumer;
import kernel.maidlab.domain.manager.dto.request.ProfileRequestDto;
import kernel.maidlab.domain.manager.dto.request.ProfileUpdateRequestDto;
import kernel.maidlab.domain.manager.dto.response.MypageResponseDto;
import kernel.maidlab.domain.manager.dto.response.ProfileResponseDto;
import kernel.maidlab.domain.manager.dto.response.ReviewListResponseDto;
import kernel.maidlab.domain.manager.entity.Manager;
import kernel.maidlab.domain.matching.dto.response.AvailableManagerResponseDto;
import kernel.maidlab.common.dto.ResponseDto;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.List;

public interface ManagerService {

	ResponseEntity<ResponseDto<Void>> createProfile(ProfileRequestDto req, HttpServletRequest httpReq);

	ResponseEntity<ResponseDto<MypageResponseDto>> getMypage(HttpServletRequest req);

	ResponseEntity<ResponseDto<ProfileResponseDto>> getProfile(HttpServletRequest req);

	ResponseEntity<ResponseDto<Void>> updateProfile(ProfileUpdateRequestDto req, HttpServletRequest httpReq);

	ResponseEntity<ResponseDto<ReviewListResponseDto>> getMyReviews(HttpServletRequest req);

	List<AvailableManagerResponseDto> findAvailableManagers(String gu, LocalDateTime StartTime, LocalDateTime EndTime);

	List<AvailableManagerResponseDto> previousManagers(Consumer consumer);

	Manager getManager(String userId);

	Manager findById(Long managerId);
}
