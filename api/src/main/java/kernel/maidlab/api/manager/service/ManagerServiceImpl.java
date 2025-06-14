package kernel.maidlab.api.manager.service;

import org.springframework.http.ResponseEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.servlet.http.HttpServletRequest;

// import jakarta.transaction.Transactional;
import kernel.maidlab.api.auth.entity.Manager;
import kernel.maidlab.api.auth.jwt.JwtDto;
import kernel.maidlab.api.auth.jwt.JwtProvider;
import kernel.maidlab.api.exception.BaseException;
import kernel.maidlab.api.manager.dto.request.*;
import kernel.maidlab.api.manager.dto.response.*;
import kernel.maidlab.api.manager.dto.object.*;
import kernel.maidlab.api.manager.entity.*;
import kernel.maidlab.api.manager.repository.*;
import kernel.maidlab.api.reservation.repository.ReviewRepository;
import kernel.maidlab.common.dto.ResponseDto;
import kernel.maidlab.common.enums.ResponseType;
import kernel.maidlab.common.enums.ServiceType;
import kernel.maidlab.common.enums.Status;
import kernel.maidlab.common.enums.UserType;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import kernel.maidlab.api.manager.dto.ManagerResponseDto;
import kernel.maidlab.api.manager.repository.ManagerRepository;
import kernel.maidlab.api.manager.dto.ManagerListResponseDto;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ManagerServiceImpl implements ManagerService {

	private final ManagerRepository managerRepository;
	private final ManagerServiceTypeRepository managerServiceTypeRepository;
	private final ManagerRegionRepository managerRegionRepository;
	private final ManagerScheduleRepository managerScheduleRepository;
	private final ManagerDocumentRepository managerDocumentRepository;
	private final RegionRepository regionRepository;
	private final ReviewRepository reviewRepository;
	private final JwtProvider jwtProvider;

	private Manager getManagerFromToken(HttpServletRequest req) {
		String accessToken = jwtProvider.extractToken(req);
		if (accessToken == null) {
			throw new BaseException(ResponseType.AUTHORIZATION_FAILED);
		}

		JwtDto.ValidationResult validationResult = jwtProvider.validateAccessToken(accessToken);
		if (!validationResult.isValid() || validationResult.getUserType() != UserType.MANAGER) {
			throw new BaseException(ResponseType.AUTHORIZATION_FAILED);
		}

		return managerRepository.findByUuid(validationResult.getUuid())
			.orElseThrow(() -> new BaseException(ResponseType.AUTHORIZATION_FAILED));
	}

	// 최초 기본 프로필 생성
	@Override
	public ResponseEntity<ResponseDto<Void>> createProfile(ProfileRequestDto req, HttpServletRequest httpReq) {
		Manager manager = getManagerFromToken(httpReq);

		if (req.getProfileImage() != null) {
			manager.updateProfileImage(req.getProfileImage());
		}

		if (req.getIntroduceText() != null) {
			manager.updateIntroduceText(req.getIntroduceText());
		}

		if (req.getServiceTypes() != null && !req.getServiceTypes().isEmpty()) {
			for (ServiceListItem serviceItem : req.getServiceTypes()) {
				try {
					ServiceType serviceTypeEnum = ServiceType.valueOf(serviceItem.getServiceType());
					ManagerServiceType managerServiceType = ManagerServiceType.managerServiceType(manager, serviceTypeEnum);
					managerServiceTypeRepository.save(managerServiceType);
				} catch (IllegalArgumentException e) {
					throw new BaseException(ResponseType.VALIDATION_FAILED);
				}
			}
		}

		if (req.getRegions() != null && !req.getRegions().isEmpty()) {
			for (RegionListItem regionItem : req.getRegions()) {
				Region region = regionRepository.findByRegionName(regionItem.getRegion())
					.orElseThrow(() -> new BaseException(ResponseType.VALIDATION_FAILED));

				ManagerRegion managerRegion = ManagerRegion.managerRegion(manager, region);
				managerRegionRepository.save(managerRegion);
			}
		}

		if (req.getAvailableTimes() != null && !req.getAvailableTimes().isEmpty()) {
			for (ScheduleListItem scheduleItem : req.getAvailableTimes()) {
				ManagerSchedule schedule = ManagerSchedule.managerSchedule(
					manager,
					scheduleItem.getDay(),
					scheduleItem.getStartTime(),
					scheduleItem.getEndTime()
				);
				managerScheduleRepository.save(schedule);
			}
		}

		if (req.getDocuments() != null && !req.getDocuments().isEmpty()) {
			for (DocumentListItem documentItem : req.getDocuments()) {
				ManagerDocument document = ManagerDocument.managerDocument(
					manager,
					documentItem.getFileType(),
					documentItem.getFileName(),
					documentItem.getUploadedFileUrl()
				);
				managerDocumentRepository.save(document);
			}
		}

		managerRepository.save(manager);

		return ResponseDto.success();
	}

	// 마이페이지 조회
	@Override
	@Transactional(readOnly = true)
	public ResponseEntity<ResponseDto<MypageResponseDto>> getMypage(HttpServletRequest req) {

		Manager manager = getManagerFromToken(req);

		Boolean isVerified = Status.APPROVED.equals(manager.getIsVerified());

		MypageResponseDto responseDto = new MypageResponseDto(
			manager.getId(),
			UserType.MANAGER,
			manager.getProfileImage(),
			manager.getName(),
			isVerified
		);

		return ResponseDto.success(responseDto);
	}

	// 프로필 조회
	@Override
	@Transactional(readOnly = true)
	public ResponseEntity<ResponseDto<ProfileResponseDto>> getProfile(HttpServletRequest req) {
		Manager manager = getManagerFromToken(req);

		List<ServiceType> services = managerServiceTypeRepository.findServiceTypesByManagerId(manager.getId());

		List<String> regionNames = managerRegionRepository.findRegionNamesByManagerId(manager.getId());
		List<RegionListItem> regions = regionNames.stream()
			.map(RegionListItem::new)
			.collect(Collectors.toList());

		List<ManagerSchedule> managerSchedules = managerScheduleRepository.findByManagerId(manager.getId());
		List<ScheduleListItem> schedules = managerSchedules.stream()
			.map(ms -> new ScheduleListItem(
				ms.getAvailableDay(),
				ms.getAvailableStartTime(),
				ms.getAvailableEndTime()
			))
			.collect(Collectors.toList());

		boolean isVerified = manager.getIsVerified() != null && manager.getIsVerified().name().equals("APPROVED");

		ProfileResponseDto responseDto = new ProfileResponseDto(
			manager.getId(),
			UserType.MANAGER,
			isVerified,
			manager.getProfileImage(),
			manager.getName(),
			manager.getBirth(),
			manager.getGender(),
			regions,
			schedules,
			services,
			manager.getIntroduceText()
		);

		return ResponseDto.success(responseDto);
	}

	// 프로필 수정
	@Override
	public ResponseEntity<ResponseDto<Void>> updateProfile(ProfileUpdateRequestDto req, HttpServletRequest httpReq) {
		Manager manager = getManagerFromToken(httpReq);

		manager.updateBasicInfo(req.getName(), req.getBirth(), req.getGender());

		if (req.getProfileImage() != null) {
			manager.updateProfileImage(req.getProfileImage());
		}

		if (req.getIntroduceText() != null) {
			manager.updateIntroduceText(req.getIntroduceText());
		}

		managerServiceTypeRepository.deleteByManagerId(manager.getId());
		managerRegionRepository.deleteByManagerId(manager.getId());
		managerScheduleRepository.deleteByManagerId(manager.getId());

		if (req.getServiceTypes() != null && !req.getServiceTypes().isEmpty()) {
			for (ServiceListItem serviceItem : req.getServiceTypes()) {
				try {
					ServiceType serviceTypeEnum = ServiceType.valueOf(serviceItem.getServiceType());
					ManagerServiceType managerServiceType = ManagerServiceType.managerServiceType(manager, serviceTypeEnum);
					managerServiceTypeRepository.save(managerServiceType);
				} catch (IllegalArgumentException e) {
					throw new BaseException(ResponseType.VALIDATION_FAILED);
				}
			}
		}

		if (req.getRegions() != null && !req.getRegions().isEmpty()) {
			for (RegionListItem regionItem : req.getRegions()) {
				Region region = regionRepository.findByRegionName(regionItem.getRegion())
					.orElseThrow(() -> new BaseException(ResponseType.VALIDATION_FAILED));

				ManagerRegion managerRegion = ManagerRegion.managerRegion(manager, region);
				managerRegionRepository.save(managerRegion);
			}
		}

		if (req.getAvailableTimes() != null && !req.getAvailableTimes().isEmpty()) {
			for (ScheduleListItem scheduleItem : req.getAvailableTimes()) {
				ManagerSchedule schedule = ManagerSchedule.managerSchedule(
					manager,
					scheduleItem.getDay(),
					scheduleItem.getStartTime(),
					scheduleItem.getEndTime()
				);
				managerScheduleRepository.save(schedule);
			}
		}

		managerRepository.save(manager);

		return ResponseDto.success();
	}

	@Override
	public Page<ManagerListResponseDto> getManagerBypage(int page, int size) {
		Pageable pageable = PageRequest.of(page, size);
		return managerRepository.findAll(pageable)
			.map(manager -> new ManagerListResponseDto(
				manager.getName(),
				manager.getUuid(),
				manager.getId()
			));
	}

	@Override
	public ManagerResponseDto getManager(Long id){
		Manager manager = managerRepository.findById(id).orElse(null);
		System.out.println(manager.getId());
		ManagerResponseDto managerResponseDto = ManagerResponseDto.builder()
			.uuid(manager.getUuid())
			.phoneNumber(manager.getPhoneNumber())
			.name(manager.getName())
			.birth(manager.getBirth())
			.gender(manager.getGender())
			.averageRate(manager.getAverageRate())
			.region(manager.getRegions())
			.isVerified(manager.getIsVerified())
			.isDeleted(manager.getIsDeleted())
			.build();
		return managerResponseDto;
	}
	@Transactional
	@Override
	public void approveManager(Long managerId) {
		Manager manager = managerRepository.findById(managerId).orElse(null);
		manager.approve();
	}

	@Transactional
	@Override
	public void rejectManager(Long managerId) {
		Manager manager = managerRepository.findById(managerId).orElse(null);
		manager.reject();
	}

	// 리뷰 목록 조회
	@Override
	@Transactional(readOnly = true)
	public ResponseEntity<ResponseDto<ReviewListResponseDto>> getMyReviews(HttpServletRequest req) {
		Manager manager = getManagerFromToken(req);

		List<Object[]> reviewData = reviewRepository.findManagerReviewDetails(manager.getId());

		List<ReviewListItem> reviewItems = reviewData.stream()
			.map(data -> new ReviewListItem(
				String.valueOf(data[0]), // reviewId
				BigDecimal.valueOf(((Number)data[1]).doubleValue()), // rating
				(String)data[2], // consumerName
				(String)data[3], // comment
				(String)data[4], // serviceType
				(String)data[5]  // serviceDetailType
			))
			.collect(Collectors.toList());

		ReviewListResponseDto responseDto = new ReviewListResponseDto(
			reviewItems
		);

		return ResponseDto.success(responseDto);
	}

	@Override
	public Page<ManagerListResponseDto> getManagerByPageWithStatus(int page, int size, Status status) {
		Pageable pageable = PageRequest.of(page, size);
		return managerRepository.findAllByIsVerified(status, pageable)
			.map(manager -> new ManagerListResponseDto(
				manager.getName(),
				manager.getUuid(),
				manager.getId()
			));
	}

}
